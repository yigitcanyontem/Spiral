#
# Build stage
#
FROM maven:3.9.1-openjdk-17 AS build
COPY . .
RUN mvn clean package -Pprod -DskipTests

#
# Package stage
#
FROM openjdk:17-jdk-alpine
COPY --from=build /target/forum-0.0.1-SNAPSHOT.jar spiral.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","spiral.jar"]
