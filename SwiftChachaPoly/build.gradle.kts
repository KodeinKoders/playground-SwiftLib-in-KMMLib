listOf("iphoneos", "iphonesimulator").forEach { sdk ->
    tasks.create<Exec>("build${sdk.capitalize()}") {
        group = "build"

        commandLine(
            "xcodebuild",
            "-project", "SwiftChachaPoly.xcodeproj",
            "-target", "SwiftChachaPoly",
            "-sdk", sdk
        )
        workingDir(projectDir)

        inputs.files(
            fileTree("$projectDir/SwiftChachaPoly.xcodeproj") { exclude("**/xcuserdata") },
            fileTree("$projectDir/SwiftChachaPoly")
        )
        outputs.files(
            fileTree("$projectDir/build/Release-${sdk}")
        )
    }
}

tasks.create<Delete>("clean") {
    group = "build"

    delete("$projectDir/build")
}
