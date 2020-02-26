FROM adoptopenjdk/openjdk8:alpine-jre
WORKDIR /opt/app
ARG JAR_FILE=target/addressbook.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT exec "java" "-Xms2048m" "-Xmx4096m" "-DIGNITE_NO_SHUTDOWN_HOOK=true" "-jar" "app.jar"