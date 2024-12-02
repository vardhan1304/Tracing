# Step 1: Build the application
FROM gradle:7.5.0-jdk17 AS build
RUN mkdir -p /workspace
WORKDIR /workspace
COPY build.gradle settings.gradle /workspace/
COPY src /workspace/src
RUN gradle clean build -x test



# Step 2: Create the runtime image
FROM openjdk:17-jdk-slim-buster
COPY --from=build /workspace/build/libs/*.jar app.jar
EXPOSE 9097
ENTRYPOINT ["java", "-jar", "app.jar"]
