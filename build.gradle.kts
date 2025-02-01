import org.jooq.meta.jaxb.*

plugins {
  kotlin("jvm") version "1.9.25"
  kotlin("plugin.spring") version "1.9.25"
  id("org.springframework.boot") version "3.4.2"
  id("io.spring.dependency-management") version "1.1.7"
  id("org.jooq.jooq-codegen-gradle") version "3.19.18"
}

group = "com.challenge.prewave"
version = "0.0.1-SNAPSHOT"

java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(21)
  }
}

jooq {
  configuration {

    jdbc {
      driver = "org.postgresql.Driver"
      url = "jdbc:postgresql://${System.getenv("DATABASE_HOST") ?: "localhost"}:${System.getenv("DATABASE_PORT") ?: "5432"}/${System.getenv("DATABASE_NAME") ?: "test"}"
      user = System.getenv("DATABASE_USERNAME") ?: "test"
      password = System.getenv("DATABASE_PASSWORD") ?: "test"
    }
    generator {
      name = "org.jooq.codegen.KotlinGenerator"
      database {
        name = "org.jooq.meta.postgres.PostgresDatabase"
        includes = ".*"
        inputSchema = "public"
      }

      generate {}
      target {
        packageName = "com.challenge.prewave.prewave_challenge"
        directory = "build/generated-src/jooq/main"
      }
    }
  }
}


repositories {
  mavenCentral()
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-jooq")
  implementation("org.springframework.boot:spring-boot-starter-validation")
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.postgresql:postgresql:42.5.4")
  implementation("com.h2database:h2")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
  jooqCodegen("org.postgresql:postgresql:42.5.4")
}

kotlin {
  compilerOptions {
    freeCompilerArgs.addAll("-Xjsr305=strict")
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
}

tasks.named("compileKotlin") {
    dependsOn(tasks.named("jooqCodegen"))
}

tasks.named("testClasses") {
    dependsOn(tasks.named("jooqCodegen"))
}