FROM openjdk:24-jdk
ADD target/GeoWeather-0.0.1-SNAPSHOT.jar GeoWeather-0.0.1-SNAPSHOT.jar
ENTRYPOINT java -jar GeoWeather-0.0.1-SNAPSHOT.jar