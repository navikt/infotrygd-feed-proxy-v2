#!/bin/sh
set -eu

CREDENTIAL_USERNAME="$(cat /var/run/secrets/nais.io/serviceuser/username)"
CREDENTIAL_PASSWORD="$(cat /var/run/secrets/nais.io/serviceuser/password)"
export CREDENTIAL_USERNAME CREDENTIAL_PASSWORD

exec java -jar /app/app.jar
