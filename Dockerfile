FROM openjdk:11
EXPOSE 8080
ADD target/pokiapi-0.0.1-SNAPSHOT.jar pokiapi-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/pokiapi-0.0.1-SNAPSHOT.jar"]