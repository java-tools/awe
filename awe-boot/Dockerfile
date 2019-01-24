# Use an official Open jdk runtime as a parent image
FROM openjdk:8-jdk-alpine

# Image maintainer
LABEL maintainer="pablo.vidal@almis.com"

# Copy the current directory contents into the container at /app
ADD target/awe-boot.jar awe-boot.jar

# Volume of app logs
VOLUME /logs

# Volume of app data
VOLUME /tmp

# Server port
EXPOSE 80

# Execute jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/awe-boot.jar"]


