#!/bin/sh
echo "running init.sh script"
export CREDENTIAL_USERNAME=$(cat /var/run/secrets/serviceuser/srvinfotrygd-feed-proxy-v2/username)
export CREDENTIAL_PASSWORD=$(cat /var/run/secrets/serviceuser/srvinfotrygd-feed-proxy-v2/password)