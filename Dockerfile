FROM openjdk:11.0.15-jre
ADD target/devices-1.0-SNAPSHOT-jar-with-dependencies.jar devices-1.0-SNAPSHOT-jar-with-dependencies.jar
EXPOSE 8080
ENTRYPOINT [ "bash", "-c", "java -jar devices-1.0-SNAPSHOT-jar-with-dependencies.jar" ]