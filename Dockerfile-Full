FROM openjdk:11 as buildstage
WORKDIR /rest-advanced
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY coverage coverage
COPY persistence persistence
COPY validation validation
COPY service service
COPY web web
COPY lombok.config .
CMD ls
RUN ./mvnw clean package spring-boot:repackage -DskipTests
COPY web/target/*.jar app.jar

FROM openjdk:11
COPY --from=buildstage /rest-advanced/app.jar .
COPY start-app.sh .
ENTRYPOINT ["./start-app.sh"]