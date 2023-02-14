FROM ghcr.io/navikt/baseimages/temurin:17

ENV APPD_ENABLED=true
ENV APP_NAME=infotrygd-feed-proxy
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75"

COPY init.sh /init-scripts/init.sh
COPY ./target/infotrygd-feed-proxy.jar "app.jar"
