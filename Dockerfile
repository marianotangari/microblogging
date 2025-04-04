FROM maven:3.8-openjdk-17 as build
WORKDIR /
COPY . .
RUN --mount=type=cache,target=/root/.m2,rw mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk
WORKDIR /
COPY --from=build /target/*.jar /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]