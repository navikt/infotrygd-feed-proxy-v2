FROM europe-north1-docker.pkg.dev/cgr-nav/pull-through/nav.no/jre:openjdk-27

ENV APP_NAME=infotrygd-feed-proxy-v2
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75"

COPY ./target/app.jar .

ENTRYPOINT ["java", "-jar", "app.jar"]
