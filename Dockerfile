FROM openjdk:17-alpine
VOLUME /tmp
ARG JAR_FILE=build/libs/oko-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java"]

CMD ["-XshowSettings:vm", \
            "-XX:+UseContainerSupport", \
            "-XX:MaxRAMPercentage=75.0", \
            "-Djava.security.egd=file:/dev/./urandom", \
            "-jar", \
            "/app.jar"]
