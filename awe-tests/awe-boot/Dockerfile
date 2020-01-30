# Use an official Open jdk runtime as a parent image
FROM openjdk:8-jdk-alpine

# Add fonts package
RUN apk add --no-cache fontconfig msttcorefonts-installer ttf-opensans && fc-cache -f

# Image maintainer
LABEL maintainer="pablo.vidal@almis.com"

# Copy the current directory contents into the container at /app
ADD target/awe-boot.war awe-boot.war

# Volume of app logs
VOLUME /logs

# Volume of app data
VOLUME /tmp

# Set JAVA options
ENV JAVA_OPTS="-Xmx512m -Xmx512m"

# Execute jar
ENTRYPOINT ["java", "-Dspring.devtools.restart.enabled=false", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/awe-boot.war"]


