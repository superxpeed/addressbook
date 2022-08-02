FROM adoptopenjdk/openjdk8:alpine-jre
WORKDIR /opt/app
ARG JAR_FILE=target/mongo-server.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT exec "java" "-Xms4096m" "-Xmx4096m" "-Dfile.encoding=UTF-8" "-Djava.io.tmpdir=/mnt/logs" "-Dlogging.config=/mnt/logback.xml" "-jar" "app.jar"