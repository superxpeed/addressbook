FROM adoptopenjdk/openjdk8:alpine-jre
WORKDIR /opt/app
ARG JAR_FILE=target/ignite-server-full.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT exec "java" "-Xms8192m" "-Xmx8192m" "-jar" "app.jar"