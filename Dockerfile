# ==========================================
# STAGE 1: Build the app using Maven
# ==========================================
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy the pom.xml and source code to the container
COPY pom.xml .
COPY src ./src

# Package the application (this creates the new .jar file)
RUN mvn clean package -DskipTests

# ==========================================
# STAGE 2: Run the app using a lightweight Java environment
# ==========================================
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy ONLY the newly built .jar file from Stage 1
COPY --from=build /app/target/studentManagementApp-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080
EXPOSE 8080

# Start the application
ENTRYPOINT ["java", "-jar", "app.jar"]