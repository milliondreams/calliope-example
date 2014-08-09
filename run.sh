#!/bin/sh

SPARK_SERVER=$1

TJCP=$(echo ${TUPLEJUMP_HOME}/tuplejump/lib-ext/*.jar | tr ' ' ':')

echo $TJCP

SPARKCP=$(echo ${TUPLEJUMP_HOME}/spark/lib/*.jar | tr ' ' ':')

echo $SPARKCP

java -cp "${TJCP}:${SPARKCP}:target/scala-2.10/calliope-example-assembly-0.1-SNAPSHOT.jar" com.tuplejump.calliope.example.HelloWorld $1 ${TUPLEJUMP_HOME}/spark
