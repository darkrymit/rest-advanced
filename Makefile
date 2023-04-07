#Windows
MVN := mvnw
#Unix
#MVN := ./mvnw

#Be aware its not a function its variable defined by multiline
define buildImageUseSpringPlugin
	$(MVN) -pl "!:coverage" clean compile spring-boot:build-image -DskipTests
endef

default:
	cat ./Makefile

clean:
	$(MVN) clean

buildImage:
	$(call buildImageUseSpringPlugin)

buildImageUsePlugin:
	$(call buildImageUseSpringPlugin)

buildImageUseDocker:
	$(MVN) -pl "!:coverage" clean package spring-boot:repackage -DskipTests
	docker  build -t spring-rest-advanced .

startAll:
	docker compose up

stopAll:
	docker compose down
