FROM ghcr.io/navikt/baseimages/temurin:21

ENV APP_NAME=infotrygd-feed-proxy-v2
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75"

COPY init.sh /init-scripts/init.sh
COPY ./target/app.jar .
