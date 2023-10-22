FROM amazoncorretto:11
COPY /target/*.jar app.jar
WORKDIR /main-service
ENTRYPOINT ["java","-jar","/app.jar"]