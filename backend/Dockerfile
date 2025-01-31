FROM openjdk:21-jdk-slim
WORKDIR /app
COPY build/libs/software-mind-recruitment-task-0.0.1-SNAPSHOT.jar /app/backend.jar
ENTRYPOINT ["java", "-jar", "backend.jar"]