#!/usr/bin/env bash

. constants.sh

UP=0
DOWN=0

SCRIPT_NAME=$0

function down() {
    docker-compose down
}

function up() {
    export HOST_NAME=`ifconfig | grep -Eo 'inet (addr:)?([0-9]*\.){3}[0-9]*' | grep -v '127.0.0.1' | head -1 | tr -s ' ' ' ' | cut -f2 -d" "`
    docker-compose up -d
    sleep 10
# common in topic with raw data
    docker run -it --rm wurstmeister/kafka:2.12-2.1.0 /opt/kafka/bin/kafka-topics.sh \
        --zookeeper ${HOST_NAME}:2181 --replication-factor 1 --partitions 1 --create --topic ${TOPIC_RAW_FLIGHT_DATA}

# flight snapshot data with landed time
    docker run -it --rm wurstmeister/kafka:2.12-2.1.0 /opt/kafka/bin/kafka-topics.sh \
        --zookeeper ${HOST_NAME}:2181 --replication-factor 1 --partitions 1 --create --topic ${TOPIC_FLIGHT_DATA_WITH_LANDED_TIME}

# airport topic
    docker run -it --rm wurstmeister/kafka:2.12-2.1.0 /opt/kafka/bin/kafka-topics.sh \
        --zookeeper ${HOST_NAME}:2181 --replication-factor 1 --partitions 1 --create --topic ${TOPIC_AIRPORT} \
        --config cleanup.policy=compact

# landed topic
    docker run -it --rm wurstmeister/kafka:2.12-2.1.0 /opt/kafka/bin/kafka-topics.sh \
        --zookeeper ${HOST_NAME}:2181 --replication-factor 1 --partitions 1 --create --topic ${TOPIC_LANDED} \
        --config retention.ms=600000
}

function on_error {
    echo "Error: $1"
    exit 1
}

function usage() {
    echo "Usage: ${SCRIPT_NAME} (--up|--down)"
}

function parseArgs() {
    while [[ $# -gt 0 ]]
    do
        case $1 in
            --up)
            UP=1
            ;;
            --down)
            DOWN=1
            ;;
            *)
            usage
            on_error "Unknwon parameter was passed"
        esac
        shift
    done

    if [ ${UP} -eq 1 -a ${DOWN} -eq 1 ]
    then
        usage
        on_error "Only one flag might be passed"
    fi

    if [ ${UP} -eq 0 -a ${DOWN} -eq 0 ]
    then
        usage
        on_error "At least one flag has to be passed"
    fi

}




#########################
#########################
## Start       ##########
#########################
#########################

parseArgs $*

if [ ${UP} -eq 1 ]
then
    up
elif [ ${DOWN} -eq 1 ]
then
    down
fi


