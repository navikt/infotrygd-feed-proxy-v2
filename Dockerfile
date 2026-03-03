FROM amazoncorretto:25.0.2-alpine3.23

ENV APP_NAME=infotrygd-feed-proxy-v2
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75"

RUN mkdir /opt/app

COPY target/*.jar /opt/app/app.jar
COPY init-scripts/init.sh /opt/app/init.sh