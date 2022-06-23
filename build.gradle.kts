import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/*
 * This file was generated by the Gradle 'init' task.
 *
 * This is a general purpose Gradle build.
 * Learn how to create Gradle builds at https://guides.gradle.org/creating-new-gradle-builds/
 */

group = "CHANGEME"
version = "0.0.1"

val applicationClassName = "CHANGEME"

val kotlinVersion = "1.6.0"
val ktorVersion = "1.6.8"
val junitVersion = "5.8.2"

plugins {
  kotlin("jvm") version "1.7.0"
  id("org.jetbrains.dokka") version "1.7.0"
  java
  application
  id("com.github.johnrengelman.shadow") version "4.0.3"
  `maven-publish`
}

repositories {
  mavenLocal()
  mavenCentral()
  maven("https://kotlin.bintray.com/ktor")
}

dependencies {
  implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.13.3")
  implementation("com.apurebase:kgraphql:0.17.14")
  implementation("io.github.microutils:kotlin-logging:2.1.23")
  implementation("io.ktor:ktor-auth:$ktorVersion")
  implementation("io.ktor:ktor-auth-jwt:$ktorVersion")
  implementation("io.ktor:ktor-jackson:$ktorVersion")
  implementation("io.ktor:ktor-metrics:$ktorVersion")
  implementation("io.ktor:ktor-server-netty:$ktorVersion")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.3")
  implementation("org.slf4j:slf4j-simple:1.7.36")
  implementation(kotlin("reflect", kotlinVersion))
  implementation(kotlin("stdlib-jdk8", kotlinVersion))
  testImplementation("com.natpryce:hamkrest:1.8.0.1")
  testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
  testImplementation("org.junit.jupiter:junit-jupiter-params:$junitVersion")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
  testRuntimeOnly("org.junit.vintage:junit-vintage-engine:$junitVersion")
  testImplementation(kotlin("test", kotlinVersion))
  testImplementation(kotlin("test-junit", kotlinVersion))
}

application {
  mainClassName = applicationClassName
}

val shadowJar by tasks.getting(ShadowJar::class) {
  manifest.attributes["Main-Class"] = applicationClassName
}

val mainSrcSet = sourceSets["main"]

val sourceJar by tasks.creating(Jar::class) {
  group = JavaBasePlugin.DOCUMENTATION_GROUP
  description = "Assembles Kotlin sources"
  archiveClassifier.set("sources")
  from(mainSrcSet.allSource)
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    jvmTarget = "1.17"
  }
}

val dokka by tasks.creating(org.jetbrains.dokka.gradle.DokkaTask::class)

val dokkaJar by tasks.creating(Jar::class) {
  group = JavaBasePlugin.DOCUMENTATION_GROUP
  description = "Assembles Kotlin docs with Dokka"
  archiveClassifier.set("javadoc")
  from(dokka)
}

tasks.withType<Test> {
  useJUnitPlatform()
  testLogging {
    events("passed", "skipped", "failed")
  }
}

publishing {
  publications {
    create("default", MavenPublication::class.java) {
      from(components["java"])
      artifact(dokkaJar)
      artifact(sourceJar)
    }
  }
  repositories {
    maven {
      url = uri("$buildDir/repository")
    }
  }
}
