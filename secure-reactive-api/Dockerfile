# ====== Build ======
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn -q -e -DskipTests dependency:go-offline

COPY src ./src
RUN mvn -q -DskipTests clean package

# ====== Run ======
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copia el jar generado (ajusta el nombre si tu jar se llama distinto)
COPY --from=build /app/target/*.jar app.jar

ENV SERVER_PORT=8080
EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar"]