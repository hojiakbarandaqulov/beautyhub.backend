FROM openjdk:17-jdk-alpine
ENV TZ=Asia/Tashkent
EXPOSE 8080
WORKDIR /app
COPY target/api.beautyhub.jar /app/api.beautyhub.jar
ENTRYPOINT ["java", "-Dserver.port=$PORT", "-jar", "/app/api.beautyhub.jar"]