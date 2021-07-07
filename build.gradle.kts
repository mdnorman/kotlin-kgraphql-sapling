import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

/*
 * This file was generated by the Gradle 'init' task.
 *
 * This is a general purpose Gradle build.
 * Learn how to create Gradle builds at https://guides.gradle.org/creating-new-gradle-builds/
 */

group = "CHANGEME"
version = "0.0.1"

val applicationClassName = "CHANGEME"

val kotlinVersion = "1.3.30"
val ktorVersion = "1.1.+"
val junitVersion = "5.4.+"

plugins {
  kotlin("jvm") version "1.3.30"
  id("org.jetbrains.dokka") version "1.5.0"
  java
  application
  id("com.github.johnrengelman.shadow") version "4.0.4"
  `maven-publish`
}

repositories {
  mavenLocal()
  jcenter()
  maven("https://kotlin.bintray.com/ktor")
}

dependencies {
  implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.9.+")
  implementation("com.github.pgutkowski:kgraphql:0.3.+")
  implementation("io.github.microutils:kotlin-logging:1.6.+")
  implementation("io.ktor:ktor-auth:$ktorVersion")
  implementation("io.ktor:ktor-auth-jwt:$ktorVersion")
  implementation("io.ktor:ktor-jackson:$ktorVersion")
  implementation("io.ktor:ktor-metrics:$ktorVersion")
  implementation("io.ktor:ktor-server-netty:$ktorVersion")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.0.+")
  implementation("org.slf4j:slf4j-simple:1.7.+")
  implementation(kotlin("reflect", kotlinVersion))
  implementation(kotlin("stdlib-jdk8", kotlinVersion))
  testImplementation("com.natpryce:hamkrest:1.7.+")
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

val dokka by tasks.getting(org.jetbrains.dokka.gradle.DokkaTask::class) {
  outputFormat = "html"
  outputDirectory = "$buildDir/javadoc"
}

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
