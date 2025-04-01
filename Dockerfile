# Stage 1: Build the application using Maven
FROM maven:3.9.4-eclipse-temurin-21 as build

# Set the working directory in the container
WORKDIR /app

# Copy the Maven project files (pom.xml) and source code (src)
COPY pom.xml .
COPY src ./src

# Build the application and skip tests
RUN mvn clean package -DskipTests

# Stage 2: Create a runtime image with a lightweight JRE
FROM eclipse-temurin:21-jre

# Set the working directory in the container
WORKDIR /app

# Copy the compiled .jar file from the build stage
COPY --from=build /app/target/GoTasks-1.0-SNAPSHOT.jar gotasks.jar

# Expose port 8080
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "gotasks.jar"]


