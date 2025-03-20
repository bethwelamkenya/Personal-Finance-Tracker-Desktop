import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.20"
    kotlin("plugin.serialization") version "1.9.20" // Add this
    id("org.jetbrains.compose")
}

group = "com.bethwelamkenya"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenLocal()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

buildscript {
    repositories {
        google()
        mavenLocal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
    dependencies {
    }
}


configurations.all {
    resolutionStrategy {
        force("org.jetbrains.kotlin:kotlin-stdlib:1.9.20")
        force("org.jetbrains.kotlin:kotlin-stdlib-common:1.9.20")
        force("org.jetbrains.kotlin:kotlin-reflect:1.9.20")
        force("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.9.20")
        force("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.20")
    }
}

dependencies {
    // Note, if you develop a library, you should use compose.desktop.common.
    // compose.desktop.currentOs should be used in launcher-sourceSet
    // (in a separate module for demo project and in testMain).
    // With compose.desktop.common you will also lose @Preview functionality
    implementation(compose.desktop.currentOs)
    implementation(compose.foundation)
    implementation(compose.material3)
    implementation(compose.materialIconsExtended)
    implementation(compose.uiTooling)
    implementation(compose.runtime)
    @OptIn(ExperimentalComposeLibrary::class)
    implementation(compose.uiTest)
    implementation(compose.desktop.uiTestJUnit4)

    implementation("app.cash.molecule:molecule-runtime:1.0.0")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.5.1")
    // Optionally, if you use JSON serialization:
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
}


compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            javaHome = file("C:/Program Files/Java/jdk-18.0.2.1").path
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb, TargetFormat.Exe)
            packageName = "personal_finance_tracker"
            packageVersion = "1.0.1-alpha" // ✅ Allowed format for Linux, macOS, and Windows

            linux {
                packageVersion = "1.0.1-alpha" // ✅ Debian allows suffixes like `-alpha`
            }
            macOS {
                packageVersion = "1.0.1-alpha" // ✅ macOS allows this format
                dmgPackageVersion = "1.0.1" // ❌ Cannot use "-alpha" here, must be MAJOR.MINOR.PATCH
            }
            windows {
                // Optional: set a custom menu name
                menu = true
                shortcut = true
                packageVersion = "1.0.1" // ❌ Windows does not allow suffixes, must be numbers only
                exePackageVersion = "1.0.1" // ✅ MAJOR.MINOR.BUILD required
            }
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "18"
    }
}

tasks.withType<JavaCompile> {
    sourceCompatibility = "18"
    targetCompatibility = "18"
}

// In build.gradle.kts
tasks.register<JavaExec>("runApp") {
    mainClass.set("MainKt")
    classpath = sourceSets["main"].runtimeClasspath
}


tasks.register<Copy>("packageWithJRE") {
    dependsOn("build")

    from("${project.layout.buildDirectory}/libs") {
        include("*.jar")
    }

    from(System.getProperty("java.home")) {
        into("runtime")
    }

    into("${project.layout.buildDirectory}/package")
}
