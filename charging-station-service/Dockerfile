FROM openjdk:8-jdk-alpine
EXPOSE 8802
COPY build/libs/charging-station-service-0.0.1-SNAPSHOT.jar charging-station-service.jar
ENTRYPOINT ["java","-jar","-Djasypt.encryptor.password=dev-secret","charging-station-service.jar", "charging_station_service"]