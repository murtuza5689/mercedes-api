FROM openjdk:8-jdk-alpine
EXPOSE 8803
COPY build/libs/parking-spot-service-0.0.1-SNAPSHOT.jar parking-spot-service.jar
ENTRYPOINT ["java","-jar","-Djasypt.encryptor.password=dev-secret","parking-spot-service.jar", "parking_spot_service"]