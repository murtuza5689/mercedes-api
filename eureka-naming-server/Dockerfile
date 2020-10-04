FROM openjdk:8-jdk-alpine
EXPOSE 8761
COPY build/libs/eureka-naming-server-0.0.1-SNAPSHOT.jar eureka-naming-server.jar
ENTRYPOINT ["java","-jar","eureka-naming-server.jar", "eureka"]