FROM adoptopenjdk/openjdk8:alpine-jre
RUN apk update && apk add curl
RUN mkdir -p /opt/app/ignite/work
RUN chmod -R 0755 /opt/app/ignite/work
WORKDIR /opt/app
ARG JAR_FILE=target/ignite-server.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT exec "java" "-Xms8192m" "-Xmx8192m" "-Dfile.encoding=UTF-8" "-Djava.io.tmpdir=/mnt/logs" "-Dlogging.config=/mnt/logback.xml" "-jar" "app.jar"