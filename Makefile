#Windows
MVN := mvnw
#Unix
#MVN := ./mvnw

#Be aware its not a function its variable defined by multiline
define buildImageUseSpringPlugin
	$(MVN) clean spring-boot:build-image
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
	$(MVN) clean package spring-boot:repackage -DskipTests
	docker build .

startImage:
	docker compose up
