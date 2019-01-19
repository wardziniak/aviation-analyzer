#!/usr/bin/env bash

UP=0
DOWN=0


#export HOST_NAME=`ifconfig | grep -Eo 'inet (addr:)?([0-9]*\.){3}[0-9]*' | grep -v '127.0.0.1' | head -1 | tr -s ' ' ' ' | cut -f2 -d" "`
#docker-compose up -d
#
#sleep 10
#
#docker run -d \
#  --name=kafka-connect \
#  -p 8083:8083 \
#  -e CONNECT_BOOTSTRAP_SERVERS=${HOST_NAME}:9092 \
#  -e CONNECT_REST_PORT=8083 \
#  -e CONNECT_GROUP_ID="quickstart" \
#  -e CONNECT_CONFIG_STORAGE_TOPIC="quickstart-config" \
#  -e CONNECT_OFFSET_STORAGE_TOPIC="quickstart-offsets" \
#  -e CONNECT_STATUS_STORAGE_TOPIC="quickstart-status" \
#  -e CONNECT_KEY_CONVERTER="org.apache.kafka.connect.json.JsonConverter" \
#  -e CONNECT_VALUE_CONVERTER="org.apache.kafka.connect.json.JsonConverter" \
#  -e CONNECT_INTERNAL_KEY_CONVERTER="org.apache.kafka.connect.json.JsonConverter" \
#  -e CONNECT_INTERNAL_VALUE_CONVERTER="org.apache.kafka.connect.json.JsonConverter" \
#  -e CONNECT_REST_ADVERTISED_HOST_NAME=${HOST_NAME} \
#  -e CONNECT_PLUGIN_PATH=/usr/share/java,/plugins \
#  -v /Users/bwardzinski/volume/aviation/kafka-connect-plugins:/plugins \
#  confluentinc/cp-kafka-connect:5.1.0
#
#
## common in topic with raw data
#docker run -it --rm wurstmeister/kafka:2.12-2.1.0 /opt/kafka/bin/kafka-topics.sh \
#  --zookeeper ${HOST_NAME}:2181 --replication-factor 1 --partitions 1 --create --topic aviation-in
#
## flight snapshot data with landed time
#docker run -it --rm wurstmeister/kafka:2.12-2.1.0 /opt/kafka/bin/kafka-topics.sh \
#  --zookeeper ${HOST_NAME}:2181 --replication-factor 1 --partitions 1 --create --topic in-air-with-landed-time
#
## airport topic
#docker run -it --rm wurstmeister/kafka:2.12-2.1.0 /opt/kafka/bin/kafka-topics.sh \
#  --zookeeper ${HOST_NAME}:2181 --replication-factor 1 --partitions 1 --create --topic airport \
#  --config cleanup.policy=compact
#
## landed topic
#docker run -it --rm wurstmeister/kafka:2.12-2.1.0 /opt/kafka/bin/kafka-topics.sh \
#  --zookeeper ${HOST_NAME}:2181 --replication-factor 1 --partitions 1 --create --topic landed \
#  --config retention.ms=600000

# LandedTableTopic
# in-air-with-landed-time

#docker run -it --rm wurstmeister/kafka:2.12-2.1.0 /opt/kafka/bin/kafka-topics.sh \
# --zookeeper ${HOST_NAME}:2181 --replication-factor 1 --partitions 1 --create --topic test1 \
# --config cleanup.policy=compact --config min.compaction.lag.ms=1000 \
# --config segment.bytes=1000 --config min.cleanable.dirty.ratio=0.1



function down() {
    docker stop kafka-connect
    docker rm kafka-connect
    docker-compose down
}

function up() {
    export HOST_NAME=`ifconfig | grep -Eo 'inet (addr:)?([0-9]*\.){3}[0-9]*' | grep -v '127.0.0.1' | head -1 | tr -s ' ' ' ' | cut -f2 -d" "`
    docker-compose up -d

    sleep 10

    docker run -d \
        --name=kafka-connect \
        -p 8083:8083 \
        -e CONNECT_BOOTSTRAP_SERVERS=${HOST_NAME}:9092 \
		-e CONNECT_REST_PORT=8083 \
		-e CONNECT_GROUP_ID="quickstart" \
		-e CONNECT_CONFIG_STORAGE_TOPIC="quickstart-config" \
		-e CONNECT_OFFSET_STORAGE_TOPIC="quickstart-offsets" \
		-e CONNECT_STATUS_STORAGE_TOPIC="quickstart-status" \
		-e CONNECT_KEY_CONVERTER="org.apache.kafka.connect.json.JsonConverter" \
		-e CONNECT_VALUE_CONVERTER="org.apache.kafka.connect.json.JsonConverter" \
		-e CONNECT_INTERNAL_KEY_CONVERTER="org.apache.kafka.connect.json.JsonConverter" \
		-e CONNECT_INTERNAL_VALUE_CONVERTER="org.apache.kafka.connect.json.JsonConverter" \
		-e CONNECT_REST_ADVERTISED_HOST_NAME=${HOST_NAME} \
		-e CONNECT_LOG4J_ROOT_LOGLEVEL=DEBUG \
		-e CONNECT_PLUGIN_PATH=/usr/share/java,/plugins \
        -v /Users/bwardzinski/volume/aviation/kafka-connect-plugins:/plugins \
        confluentinc/cp-kafka-connect:5.1.0


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
}

function parseArgs() {
    while [[ $# -gt 0 ]]
    do
        echo "$1"
        case $1 in
            --up)
            UP=1
            ;;
            --down)
            DOWN=1
            ;;
            *)
            echo "Unknwon parameter was passed"
        esac
        shift
    done

    if [ ${UP} -eq 1 -a ${DOWN} -eq 1 ]
    then
        echo "ERROR: 1"
    fi

    if [ ${UP} -eq 0 -a ${DOWN} -eq 0 ]
    then
        echo "ERROR: 0"
    fi

}

parseArgs $*

if [ ${UP} -eq 1 ]
then
    up
elif [ ${DOWN} -eq 1 ]
then
    down
fi


