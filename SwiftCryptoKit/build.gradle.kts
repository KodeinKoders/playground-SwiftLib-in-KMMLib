listOf("iphoneos", "iphonesimulator").forEach { sdk ->
    tasks.create<Exec>("buildCrypto${sdk.capitalize()}") {
        group = "build"

        commandLine(
            "xcodebuild",
            "-project", "SwiftCryptoKit.xcodeproj",
            "-target", "SwiftCryptoKit",
            "-sdk", sdk
        )
        workingDir(projectDir)

        inputs.files(
            fileTree("$projectDir/SwiftCryptoKit.xcodeproj") { exclude("**/xcuserdata") },
            fileTree("$projectDir/SwiftCryptoKit")
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
