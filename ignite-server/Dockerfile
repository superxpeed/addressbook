FROM adoptopenjdk/openjdk8:alpine-jre as builder
WORKDIR application
ARG JAR_FILE=target/ignite-server.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract
RUN java -version

FROM adoptopenjdk/openjdk8:alpine-jre
RUN mkdir -p application/ignite/work
RUN chmod -R 0755 application/ignite/work
WORKDIR application
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./
ENV java_opts=""
CMD java $java_opts org.springframework.boot.loader.JarLauncher