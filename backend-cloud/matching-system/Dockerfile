FROM openjdk:18-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} matching.jar
ENTRYPOINT ["java","-jar","/matching.jar"]