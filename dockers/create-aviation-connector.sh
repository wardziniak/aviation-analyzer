#!/usr/bin/env bash

. constants.sh

export HOST_NAME=`ifconfig | grep -Eo 'inet (addr:)?([0-9]*\.){3}[0-9]*' | grep -v '127.0.0.1' | head -1 | tr -s ' ' ' ' | cut -f2 -d" "`

curl -XDELETE ${HOST_NAME}:8083/connectors/aviation-connector

curl -0 -v -X POST ${HOST_NAME}:8083/connectors \
-H 'Content-Type: application/json' \
-d @- << EOF

{
  "name": "aviation-connector",
  "config": {
  	"connector.class": "at.grahsl.kafka.connect.mongodb.MongoDbSinkConnector",
  	"key.converter": "com.wardziniak.aviation.synchronizer.kafka.converter.FlightNumberKeyConverter",
  	"value.converter": "com.wardziniak.aviation.synchronizer.kafka.converter.LandedFLightValueConverter",
    "topics": "${TOPIC_FLIGHT_DATA_WITH_LANDED_TIME}",
    "mongodb.connection.uri": "mongodb://${HOST_NAME}:27017/test?w=1&journal=true",
    "mongodb.collection": "${MONGODB_COLL_FLIGHT_DATA_WITH_LANDED_TIME}",
    "mongodb.collections": "${MONGODB_COLL_FLIGHT_DATA_WITH_LANDED_TIME}",
    "mongodb.key.projection.type": "blacklist",
	"mongodb.key.projection.list": "*"
  }
}
EOF