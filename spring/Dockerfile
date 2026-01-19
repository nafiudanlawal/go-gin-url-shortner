FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .

COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-noble

WORKDIR /app

RUN groupadd --system springGroup && \
	useradd --system --gid springGroup --create-home --shell /usr/sbin/nologin springUser

USER springUser

ARG JAR_FILE=app/target/url-shortner-service-0.0.1-SNAPSHOT.jar

COPY --from=build --chown=springUser:springGroup ${JAR_FILE} app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]