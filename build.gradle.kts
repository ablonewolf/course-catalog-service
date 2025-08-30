plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.5.5"
    id("io.spring.dependency-management") version "1.1.7"
    kotlin("plugin.jpa") version "1.9.25"
}

group = "org.ablonewolf"
version = "0.0.1-SNAPSHOT"
description = "course-catalog-service"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // dependency for OpenAPI documentation
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.10")

    runtimeOnly("org.postgresql:postgresql")

    // Exclude mockito-core from spring-boot-starter-test
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.mockito", module = "mockito-core")
        exclude(group = "org.mockito", module = "mockito-junit-jupiter")
    }

    testImplementation("org.springframework.boot:spring-boot-starter-webflux")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")

    // we are using mockito-kotlin instead of mockito-core since it provides a better kotlin support
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // https://mvnrepository.com/artifact/org.testcontainers/junit-jupiter
    testImplementation("org.testcontainers:junit-jupiter:1.21.3")

    // https://mvnrepository.com/artifact/org.testcontainers/postgresql
    testImplementation("org.testcontainers:postgresql:1.21.3")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

sourceSets {
    test {
        java {
            srcDir("src/test/unit")
            srcDir("src/test/integration")
        }
    }
}
