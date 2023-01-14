import java.io.FileInputStream
import java.util.*
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    id("org.jetbrains.intellij") version "1.12.0"
    id("org.jetbrains.kotlin.jvm") version "1.8.0"
    id("org.jetbrains.kotlin.kapt") version "1.8.0"
}

group = ("com.project.iosephknecht")
version = "v1.0.0-alpha01-221.6008.13"

val idePathKey = "ANDROID_STUDIO_PATH"
val platformPrefix = "AndroidStudio"
val localProperties = Properties()

localProperties.load(FileInputStream(rootProject.file("local.properties")))

repositories {
    mavenCentral()
    google()
}

ext {
    set("kotlin_version", "1.8.0")
    set("rx_java_version", "3.1.5")
    set("rx_swing_version", "3.1.1")
    set("dagger_version", "2.43.2")
    set("junit_version", "5.9.0")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${project.ext.get("kotlin_version")}")
    implementation("io.reactivex.rxjava3:rxjava:${project.ext.get("rx_java_version")}")
    implementation("com.github.akarnokd:rxjava3-swing:${project.ext.get("rx_swing_version")}")

    implementation("com.google.dagger:dagger:${project.ext.get("dagger_version")}")

    runtimeOnly("org.jetbrains.kotlin:kotlin-reflect:${project.ext.get("kotlin_version")}")

    testImplementation("org.junit.jupiter:junit-jupiter-api:${project.ext.get("junit_version")}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${project.ext.get("junit_version")}")

    kapt("com.google.dagger:dagger-compiler:${project.ext.get("dagger_version")}")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    version.set("221.6008.13")
    type.set("IC")
    this.plugins.add("android")
}

tasks.buildSearchableOptions {
    enabled = false
}
tasks.patchPluginXml {
    changeNotes.set(
        """
      - Raising the intellij version to 221.6008.13. <br>
      """
    )
}
tasks.runIde {
    if (localProperties.containsKey(idePathKey)) {
        systemProperty("idea.platform.prefix", platformPrefix)
        ideDir.set(file(localProperties.getProperty(idePathKey)))
    }
}
tasks.withType(KotlinJvmCompile::class.java) {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_11.toString()
}
tasks.compileKotlin {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_11.toString()
}
tasks.compileTestKotlin {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_11.toString()
}
tasks.test {
    useJUnitPlatform()
}