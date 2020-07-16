FROM openjdk:14-alpine
COPY target/currency-rates-api-*.jar currency-rates-api.jar
EXPOSE 8080
CMD ["java", "-Dcom.sun.management.jmxremote", "-Xmx128m", "-jar", "currency-rates-api.jar"]
