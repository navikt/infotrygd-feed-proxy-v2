#!/bin/sh
echo "running init.sh script"
export CREDENTIAL_USERNAME=$(cat /var/run/secrets/serviceuser/srvinfotrygd-feed/username)
export CREDENTIAL_PASSWORD=$(cat /var/run/secrets/serviceuser/srvinfotrygd-feed/password)