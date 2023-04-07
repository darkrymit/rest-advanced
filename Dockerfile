#Require: mvn clean package spring-boot:repackage -DskipTests
FROM adoptopenjdk:11-jre-hotspot as builder
COPY start-app.sh .
COPY web/target/*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

FROM adoptopenjdk:11-jre-hotspot
COPY --from=builder dependencies/ ./
COPY --from=builder snapshot-dependencies/ ./
COPY --from=builder spring-boot-loader/ ./
COPY --from=builder application/ ./
COPY --from=builder start-app.sh .
ENTRYPOINT ["./start-app.sh"]