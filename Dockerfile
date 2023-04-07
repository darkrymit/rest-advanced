#Require: mvn clean package spring-boot:repackage -DskipTests
FROM openjdk:11
COPY web/target/*.jar app.jar
COPY start-app.sh .
ENTRYPOINT ["./start-app.sh"]