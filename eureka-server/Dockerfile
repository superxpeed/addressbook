FROM adoptopenjdk/openjdk8:alpine-jre
WORKDIR /opt/app
ARG JAR_FILE=target/eureka-server.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT exec "java" "-Xms2048m" "-Xmx4096m" "-jar" "app.jar"