FROM openjdk:8-jre-alpine AS base
WORKDIR /app
EXPOSE 8080

FROM maven:3.5.0-jdk-8-alpine AS build
WORKDIR /src
COPY ["pom.xml", "."]
COPY [".m2/settings.xml", ".m2/settings.xml"]
RUN mvn -s .m2/settings.xml clean install
COPY . .
RUN mvn -s .m2/settings.xml package

FROM base AS final
WORKDIR /app
COPY --from=build /src/target/RouteTraceService-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-Djava.net.preferIPv4Stack=true", "-jar","app.jar"]
