#Windows
MVN := mvnw
#Unix
#MVN := ./mvnw

#Be aware its not a function its variable defined by multiline
define buildImageUseSpringBuildImage
	$(MVN) -pl "!:coverage" clean install spring-boot:build-image -DskipTests
endef
define buildImageUseSpringPackageAndDockerFile
	$(MVN) -pl "!:coverage" clean package -DskipTests
    docker  build -t spring-rest-advanced .
endef

default:
	cat ./Makefile

clean:
	$(MVN) clean

buildImage:
	$(call buildImageUseSpringPackageAndDockerFile)

buildImageUsePlugin:
	$(call buildImageUseSpringBuildImage)

buildImageUseDocker:
	$(call buildImageUseSpringPackageAndDockerFile)

startAll:
	docker compose up

stopAll:
	docker compose down
