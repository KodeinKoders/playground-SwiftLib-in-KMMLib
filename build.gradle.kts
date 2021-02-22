plugins {
    kotlin("multiplatform") version "1.4.30"
}

group = "org.demo"
version = "1.0"

repositories {
    mavenCentral()
}

kotlin {
    jvm()

    ios {
        val platform = when (name) {
            "iosX64" -> "iphonesimulator"
            "iosArm64" -> "iphoneos"
            else -> error("Unsupported target $name")
        }
        compilations.getByName("main") {
            cinterops.create("SwiftChachaPoly") {
                val interopTask = tasks[interopProcessingTaskName]
                interopTask.dependsOn(":SwiftChachaPoly:build${platform.capitalize()}")
                includeDirs.headerFilterOnly("$rootDir/SwiftChachaPoly/build/Release-$platform/include")
            }
        }
    }
    sourceSets {
        getByName("commonTest").dependencies {
            implementation(kotlin("test-common"))
            implementation(kotlin("test-annotations-common"))
        }

        getByName("jvmTest").dependencies {
            implementation(kotlin("test-junit"))
        }

        all {
            languageSettings.useExperimentalAnnotation("kotlin.RequiresOptIn")
        }
    }
}
