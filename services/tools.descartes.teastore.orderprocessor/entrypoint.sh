#!/usr/bin/env bash

MAX_JVM_HEAP_SIZE=${MAX_JVM_HEAP:=512m}
export registryURL="http://${REGISTRY_HOST:-localhost}:${REGISTRY_PORT:-8080}/tools.descartes.teastore.registry/rest/services/"
echo "Registry URL: $registryURL"

JAVA_OPTS="${JAVA_OPTS} -Xms64m -Xmx${MAX_JVM_HEAP_SIZE} -XX:MaxPermSize=256m"
echo "JAVA_OPTS: ${JAVA_OPTS}"

java $JAVA_OPTS -jar /orderprocessor-1.0.jar