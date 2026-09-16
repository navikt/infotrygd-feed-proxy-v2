FROM europe-north1-docker.pkg.dev/cgr-nav/pull-through/nav.no/jre:openjdk-26-dev

ENV APP_NAME=infotrygd-feed-proxy-v2
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75"

WORKDIR /app

COPY ./target/app.jar ./app.jar
COPY --chown=nonroot:nonroot --chmod=0555 init.sh ./init.sh

ENTRYPOINT ["/bin/sh", "/app/init.sh"]