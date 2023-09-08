FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAVA_OPTS
ENV JAVA_OPTS=$JAVA_OPTS
COPY swim-jumpstart.jar swimjumpstart.jar
ENTRYPOINT exec java $JAVA_OPTS -jar swimjumpstart.jar
