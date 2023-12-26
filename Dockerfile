# Use an official Maven image as a base image
FROM maven:3.8-openjdk-11 AS builder

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml file to the container
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline

# Copy the entire project to the container
COPY . .

# Build the project

RUN mvn install

RUN mvn package
# Specify the command to run on container start
ENTRYPOINT ["java", "-jar", "/app/target/proj2023-0.0.1.jar"]
