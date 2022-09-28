FROM openjdk:18-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} botrunning.jar
ENTRYPOINT ["java","-jar","/botrunning.jar"]