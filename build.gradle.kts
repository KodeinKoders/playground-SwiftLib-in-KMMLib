plugins {
    kotlin("multiplatform") version "1.4.21"
}

group = "org.demo"
version = "1.0"

repositories {
    mavenCentral()
}

kotlin {
    iosX64("ios") {
        compilations.getByName("main") {
            cinterops.create("SwiftCryptoKit") {
                val interopTask = tasks[interopProcessingTaskName]
                interopTask.dependsOn(":SwiftCryptoKit:buildCryptoReleaseIphonesimulator")
                includeDirs.headerFilterOnly("$rootDir/SwiftCryptoKit/build/Release-iphonesimulator/include")
            }

        }
    }
    sourceSets {
        val iosMain by getting {

        }

        all {
            languageSettings.useExperimentalAnnotation("kotlin.RequiresOptIn")
        }
    }
}
