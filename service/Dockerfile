FROM gradle:8.14.2-jdk21 AS build
LABEL authors="venya"

WORKDIR /app

COPY build.gradle settings.gradle ./
RUN gradle dependencies --no-daemon
COPY . .

RUN gradle clean build -x test --no-daemon

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8000

CMD ["java", "-jar", "app.jar"]
