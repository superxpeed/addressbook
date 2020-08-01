FROM adoptopenjdk/openjdk8:alpine-jre
WORKDIR /opt/app
ARG JAR_FILE=target/mongo-server.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT exec "java" "-Xms4096m" "-Xmx4096m" "-jar" "app.jar"