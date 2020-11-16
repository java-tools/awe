# Use an official Open jdk runtime as a parent image
FROM openjdk:11.0.4-jre-slim

# Add fonts package
RUN apt-get install fontconfig msttcorefonts-installer ttf-opensans && fc-cache -f

# Image maintainer
LABEL maintainer="awe@almis.com"

# Copy the current directory contents into the container at /app
ADD target/awe-boot.war awe-boot.war

# Volume of app logs
VOLUME /logs

# Volume of app data
VOLUME /tmp

# Execute jar
ENTRYPOINT ["java", "-Xms256m", "-Xmx1g", "-XshowSettings:vm", "-Dspring.devtools.restart.enabled=false", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/awe-boot.war"]


