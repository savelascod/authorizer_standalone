FROM gradle:6.8.3-jdk8-openj9 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle clean build --no-daemon

FROM openjdk:8-jre-slim

EXPOSE 8080

RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/*.jar /app/authorizer_standalone.jar

ENTRYPOINT ["java","-jar","/app/authorizer_standalone.jar"]