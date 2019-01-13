#!/usr/bin/env bash


export HOST_NAME=`ifconfig | grep -Eo 'inet (addr:)?([0-9]*\.){3}[0-9]*' | grep -v '127.0.0.1' | head -1 | tr -s ' ' ' ' | cut -f2 -d" "`
docker-compose up -d

sleep 8

# common in topic with raw data
docker run -it --rm wurstmeister/kafka:2.12-2.1.0 /opt/kafka/bin/kafka-topics.sh \
  --zookeeper ${HOST_NAME}:2181 --replication-factor 1 --partitions 1 --create --topic aviation-in

# flight snapshot data with landed time
docker run -it --rm wurstmeister/kafka:2.12-2.1.0 /opt/kafka/bin/kafka-topics.sh \
  --zookeeper ${HOST_NAME}:2181 --replication-factor 1 --partitions 1 --create --topic in-air-with-landed-time

# airport topic
docker run -it --rm wurstmeister/kafka:2.12-2.1.0 /opt/kafka/bin/kafka-topics.sh \
  --zookeeper ${HOST_NAME}:2181 --replication-factor 1 --partitions 1 --create --topic airport \
  --config cleanup.policy=compact

# landed topic
docker run -it --rm wurstmeister/kafka:2.12-2.1.0 /opt/kafka/bin/kafka-topics.sh \
  --zookeeper ${HOST_NAME}:2181 --replication-factor 1 --partitions 1 --create --topic landed \
  --config retention.ms=600000

# LandedTableTopic
# in-air-with-landed-time

#docker run -it --rm wurstmeister/kafka:2.12-2.1.0 /opt/kafka/bin/kafka-topics.sh \
# --zookeeper ${HOST_NAME}:2181 --replication-factor 1 --partitions 1 --create --topic test1 \
# --config cleanup.policy=compact --config min.compaction.lag.ms=1000 \
# --config segment.bytes=1000 --config min.cleanable.dirty.ratio=0.1




