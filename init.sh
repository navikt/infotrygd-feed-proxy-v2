#!/bin/sh
set -eu

export CREDENTIAL_USERNAME="$(cat /var/run/secrets/nais.io/serviceuser/username)"
export CREDENTIAL_PASSWORD="$(cat /var/run/secrets/nais.io/serviceuser/password)"

exec java -jar /app/app.jar
