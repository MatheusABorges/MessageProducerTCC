# Stage 1: Build the application
FROM openjdk:21-slim as build

# Install Gradle
RUN apt-get update && apt-get install -y wget unzip
RUN wget https://services.gradle.org/distributions/gradle-8.1-bin.zip -P /tmp && \
    unzip -d /opt/gradle /tmp/gradle-8.1-bin.zip && \
    ln -s /opt/gradle/gradle-8.1/bin/gradle /usr/bin/gradle

# Set the working directory
WORKDIR /app

# Copy the Gradle wrapper and application source code to the container
COPY gradlew .
COPY gradle/ gradle/
COPY build.gradle .
COPY settings.gradle .
COPY src/ src/

# Make the Gradle wrapper executable
RUN chmod +x gradlew

# Build the application
RUN ./gradlew build --no-daemon

# Stage 2: Create the runtime image
FROM openjdk:21-slim

# Set the working directory
WORKDIR /app

# Copy the jar file from the build stage
COPY --from=build /app/build/libs/message-producer.jar app.jar

# Expose the port your application runs on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]