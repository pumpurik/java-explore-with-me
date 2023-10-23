FROM amazoncorretto:11
COPY /target/*.jar /app.jar
WORKDIR /stats-server
ENTRYPOINT ["java","-jar","/app.jar"]