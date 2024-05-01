plugins {
    id("java")
    id("org.openapi.generator") version "7.5.0"
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "com.pyruby"
version = "1.0-SNAPSHOT"


java {
    sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springdoc:springdoc-openapi-ui:1.6.14")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:3.21.0")
}

sourceSets {
    main {
        java {
            // TODO: Set this path according to what was generated for you
            srcDir("$projectDir/build/generated/sources/openapi/src/main/java")
        }
    }
}

openApiGenerate {
    generatorName.set("spring")
    inputSpec.set("$projectDir/src/main/resources/maze-api.yaml")
    outputDir.set("$projectDir/build/generated/sources/openapi")
    apiPackage.set("com.pyruby.maze.api")
    modelPackage.set("com.pyruby.maze.model")
    configFile.set("src/main/resources/api-config.json")
}

tasks.test {
    useJUnitPlatform()
}
