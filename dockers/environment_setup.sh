#!/usr/bin/env bash

UP=0
DOWN=0

function down() {
    docker-compose down
}

function up() {
    export HOST_NAME=`ifconfig | grep -Eo 'inet (addr:)?([0-9]*\.){3}[0-9]*' | grep -v '127.0.0.1' | head -1 | tr -s ' ' ' ' | cut -f2 -d" "`
    docker-compose up -d
    sleep 10
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


