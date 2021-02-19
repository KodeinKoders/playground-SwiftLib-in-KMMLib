plugins {
    kotlin("multiplatform") version "1.4.30"
}

group = "org.demo"
version = "1.0"

repositories {
    mavenCentral()
}

kotlin {
    ios {
        val platform = when (name) {
            "iosX64" -> "iphonesimulator"
            "iosArm64" -> "iphoneos"
            else -> error("Unsupported target $name")
        }
        compilations.getByName("main") {
            cinterops.create("SwiftCryptoKit") {
                val interopTask = tasks[interopProcessingTaskName]
                interopTask.dependsOn(":SwiftCryptoKit:buildCrypto${platform.capitalize()}")
                includeDirs.headerFilterOnly("$rootDir/SwiftCryptoKit/build/Release-$platform/include")
            }
        }
    }
    sourceSets {
        all {
            languageSettings.useExperimentalAnnotation("kotlin.RequiresOptIn")
        }
    }
}
