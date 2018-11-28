/*
 * This file was generated by the Gradle 'init' task.
 *
 * This is a general purpose Gradle build.
 * Learn how to create Gradle builds at https://guides.gradle.org/creating-new-gradle-builds/
 */

group = "com.denormans"
version = "0.0.1"

val kotlinVersion = "1.3.0"

plugins {
  kotlin("jvm") version "1.3.0"
  id("org.jetbrains.dokka") version "0.9.16"
  java
  `maven-publish`
}

repositories {
  jcenter()
}

dependencies {
  implementation(kotlin("stdlib-jdk8", kotlinVersion))
  implementation(kotlin("reflect", kotlinVersion))
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.0.+")
  implementation("org.slf4j:slf4j-simple:1.7.+")
  implementation("io.github.microutils:kotlin-logging:1.5.+")
  testImplementation("junit:junit:4.+")
  testImplementation("com.natpryce:hamkrest:1.4.+")
  testImplementation(kotlin("test", kotlinVersion))
  testImplementation(kotlin("test-junit", kotlinVersion))
}

val mainSrcSet = sourceSets["main"]

val sourceJar by tasks.creating(Jar::class) {
  group = JavaBasePlugin.DOCUMENTATION_GROUP
  description = "Assembles Kotlin sources"
  classifier = "sources"
  from(mainSrcSet.allSource)
}

val dokka by tasks.getting(org.jetbrains.dokka.gradle.DokkaTask::class) {
  outputFormat = "html"
  outputDirectory = "$buildDir/javadoc"
}

val dokkaJar by tasks.creating(Jar::class) {
  group = JavaBasePlugin.DOCUMENTATION_GROUP
  description = "Assembles Kotlin docs with Dokka"
  classifier = "javadoc"
  from(dokka)
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
