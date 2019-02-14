package com.wardziniak.aviation.processing.internal.windows

import java.nio.ByteBuffer
import java.time.Duration
import java.util.concurrent.atomic.AtomicInteger

import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.common.serialization.Serdes.BytesSerde
import org.apache.kafka.common.utils.Bytes
import org.apache.kafka.streams.kstream.{Aggregator, Transformer}
import org.apache.kafka.streams.processor.{ProcessorContext, Punctuator}
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.kstream.KStream
import org.apache.kafka.streams.state.{KeyValueStore, StoreBuilder, Stores}
import org.apache.kafka.streams.{KeyValue, StreamsBuilder => StreamsBuilderJ}

class CustomWindowedTransformerBuilder(inner: StreamsBuilderJ = new StreamsBuilderJ) {

  import CustomWindowedTransformerBuilder._

//  def aggregateWithCustomWindow[K, V, VR >: Null](
//    builder: StreamsBuilder,
//    aggregator: Aggregator[_ >: K, _ >: V, VR ],
//    closingWindowPredicate: (K, V, VR) => Boolean,
//    expirationTimeout: Duration,
//    keySerde: Serde[K],
//    aggValueSerde: Serde[VR]): Transformer[K, V, KeyValue[K, VR]] =  {
//
//    val customWindowedStoreName = "CUSTOM-WINDOWED-000001"//newStoreName(CustomWindowedName)
//    val customWindowedStore: StoreBuilder[KeyValueStore[K, Bytes]] =
//      Stores.keyValueStoreBuilder(Stores.persistentKeyValueStore(customWindowedStoreName), keySerde, VALUE_SERDE)
//    inner.addStateStore(customWindowedStore)
//
//    CustomWindowedTransformerBuilder.CustomWindowedTransformer[K, V, VR](
//      initializer, aggregator, closingWindowPredicate, expirationTimeout, customWindowedStoreName, aggValueSerde)
//  }
}

object CustomWindowedTransformerBuilder {

  private val VALUE_SERDE = new BytesSerde
  private val CustomWindowedName = "CUSTOM-WINDOWED-"
  private val index = new AtomicInteger(0)


  def aggregationWithCustomWindow[K, V, VR >: Null](stream: KStream[K,V], builder: StreamsBuilder)(
    initializer: () => VR,
    aggregator: Aggregator[_ >: K, _ >: V, VR ],
    closingWindowPredicate: (K, V, VR) => Boolean,
    expirationTimeout: Duration,
    keySerde: Serde[K],
    aggValueSerde: Serde[VR]): KStream[K, VR] =  {

    val customWindowedStoreName = newStoreName(CustomWindowedName)
    val customWindowedStore: StoreBuilder[KeyValueStore[K, Bytes]] =
      Stores.keyValueStoreBuilder(Stores.persistentKeyValueStore(customWindowedStoreName), keySerde, VALUE_SERDE)
    builder.addStateStore(customWindowedStore)

    val transformer: CustomWindowedTransformer[K, V, VR] = CustomWindowedTransformerBuilder.CustomWindowedTransformer[K, V, VR](
      initializer, aggregator, closingWindowPredicate, expirationTimeout, customWindowedStoreName, aggValueSerde)
    stream.transform(() => transformer, customWindowedStoreName)
  }

  def newStoreName(prefix: String): String = prefix + String.format("%010d", new java.lang.Integer(index.getAndIncrement))

  /**
    * It is custom windowed transformation algorithm is as follow:
    *  1. for each record:
    *   1.1 If for particular key aggregated value in store is expired (lastUpdateTimestamp + expirationTimeout > currentTimestamp),
    *       pass aggregated value and clear store for the key
    *   1.2 Calculate new aggregated value based on key: K, value: V, oldAgg: VR
    *   1.3 Check closingWindowPredicate for key: K, value: V, oldAgg: VR
    *     1.3.1 if true pass value and not update store
    *     1.3.2 if false put newAgg in store
    * @param aggregator way of adding new V to already aggregated
    * @param customWindowedStoreName name of store name - internal use
    * @param aggValueSerde valueR serde
    * @tparam K
    * @tparam V
    * @tparam VR
    */
  case class CustomWindowedTransformer[K, V, VR >: Null](
    initializer: () => VR,
    aggregator: Aggregator[_ >: K, _ >: V, VR],
    closingWindowPredicate: (K, V, VR) => Boolean,
    expirationTimeout: Duration,
    customWindowedStoreName: String,
    aggValueSerde: Serde[VR]) extends Transformer[K, V, KeyValue[K, VR]] {

    var customWindowedStore: KeyValueStore[K, Bytes] = _
    var context: ProcessorContext = _

    override def init(context: ProcessorContext): Unit = {
      this.context = context
      customWindowedStore = context.getStateStore(customWindowedStoreName).asInstanceOf[KeyValueStore[K, Bytes]]
    }

    override def transform(key: K, value: V): KeyValue[K, VR] = {
      val currentTimestamp = context.timestamp()
      val oldAggRaw = customWindowedStore.get(key)
      var oldAgg: VR = null

      // If there were old values
      if (oldAggRaw != null) {
        val rawValueR: Array[Byte] = oldAggRaw.get()
        val vrWithTimestamp = ByteBuffer.wrap(rawValueR)
        val lastUpdateTimestamp = vrWithTimestamp.getLong

        val vr = new Array[Byte](rawValueR.length - 8)
        vrWithTimestamp.get(vr)
        oldAgg = aggValueSerde.deserializer().deserialize(null, vr)
        // If timestamp expired pass forward and reset
        if (lastUpdateTimestamp + expirationTimeout.toMillis < currentTimestamp) {
          context.forward(key, oldAgg)
          customWindowedStore.delete(key)
          oldAgg = initializer()
        }
      }
      else
        oldAgg = initializer()

      var newAgg = aggregator.apply(key, value, oldAgg)
      // if predicate true pass forward and reset
      if (closingWindowPredicate(key, value, newAgg)) {
        context.forward(key, newAgg)
        customWindowedStore.delete(key)
        newAgg = initializer()
      }
      val innerValue = aggValueSerde.serializer.serialize(null, newAgg)
      val timeAndValue = ByteBuffer.wrap(new Array[Byte](8 + innerValue.length))
          .putLong(currentTimestamp)
          .put(innerValue).array()
      customWindowedStore.put(key, Bytes.wrap(timeAndValue))
      null
    }

    override def close(): Unit = {

    }
  }

  case class CustomWindowedPunctuator[K]() extends Punctuator {

    var customWindowedStore: KeyValueStore[K, Bytes] = _
    var context: ProcessorContext = _

    override def punctuate(timestamp: Long): Unit = {

    }
  }
}
