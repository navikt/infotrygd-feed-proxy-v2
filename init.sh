#!/bin/sh
echo "running init.sh script"

if test -f /var/run/secrets/nais.io/serviceuser/username;
then
  export CREDENTIAL_USERNAME=$(cat /var/run/secrets/nais.io/serviceuser/username)
  echo "- exporting CREDENTIAL_USERNAME"
fi

if test -f /var/run/secrets/nais.io/serviceuser/password;
then
  export CREDENTIAL_PASSWORD=$(cat /var/run/secrets/nais.io/serviceuser/password)
  echo "- exporting CREDENTIAL_PASSWORD"
fi