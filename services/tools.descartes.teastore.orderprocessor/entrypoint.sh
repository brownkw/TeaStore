#!/usr/bin/env bash

export registryURL="http://${REGISTRY_HOST:-localhost}:${REGISTRY_PORT:-8080}/tools.descartes.teastore.registry/rest/services/"
echo "Registry URL: $registryURL"

java -jar /orderprocessor-1.0.jar