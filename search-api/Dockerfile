FROM openjdk:8-jdk-alpine
EXPOSE 8081
COPY build/libs/search-api-0.0.1-SNAPSHOT.jar search-api.jar
ENTRYPOINT ["java","-jar","-Djasypt.encryptor.password=dev-secret","search-api.jar", "search_api"]