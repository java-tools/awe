# Use an official Open jdk runtime as a parent image
FROM openjdk:8-jdk-alpine

# Add fonts package
RUN apk add --no-cache fontconfig msttcorefonts-installer ttf-opensans && fc-cache -f

# Image maintainer
LABEL maintainer="awe@almis.com"

# Copy the current directory contents into the container at /app
ADD target/awe-boot.war awe-boot.war

# Volume of app logs
VOLUME /logs

# Volume of app data
VOLUME /tmp

# Execute jar
ENTRYPOINT ["java", "-Xms512m", "-Xmx512m", "-XshowSettings:vm", "-Dspring.devtools.restart.enabled=false", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/awe-boot.war"]


