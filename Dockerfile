FROM openjdk:jdk-alpine
VOLUME /tmp
ADD build/libs/*.jar ./app.jar
RUN sh -c 'touch /app.jar'
EXPOSE 5005
EXPOSE 8080
ENV JAVA_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 -Djava.security.egd=file:/dev/./urandom"
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar /app.jar" ]