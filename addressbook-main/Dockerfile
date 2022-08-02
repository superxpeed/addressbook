FROM adoptopenjdk/openjdk8:alpine-jre as builder
WORKDIR application
ARG JAR_FILE=target/addressbook.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract
RUN java -version

FROM adoptopenjdk/openjdk8:alpine-jre
WORKDIR application
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./
ENTRYPOINT ["java", "-Xms2048m", "-Xmx4096m", "-Dfile.encoding=UTF-8", "-Djava.io.tmpdir=/mnt/logs", "-Dlogging.config=/mnt/logback.xml", "-DIGNITE_NO_SHUTDOWN_HOOK=true", "org.springframework.boot.loader.JarLauncher"]