FROM openjdk:8-jdk-alpine
EXPOSE 8801
COPY build/libs/restaurant-service-0.0.1-SNAPSHOT.jar restaurant-service.jar
ENTRYPOINT ["java","-jar","-Djasypt.encryptor.password=dev-secret","restaurant-service.jar", "restaurant_service"]