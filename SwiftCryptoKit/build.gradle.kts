listOf("iphoneos", "iphonesimulator").forEach { sdk ->
    listOf("Debug", "Release").forEach { conf ->
        tasks.create<Exec>("buildCrypto${conf}${sdk.capitalize()}") {
            group = "build"

            commandLine(
                "xcodebuild",
                "-project", "SwiftCryptoKit.xcodeproj",
                "-target", "SwiftCryptoKit",
                "-sdk", sdk,
                "-configuration", conf
            )
            workingDir(projectDir)

            inputs.files(
                fileTree("$projectDir/SwiftCryptoKit.xcodeproj") { exclude("**/xcuserdata") },
                fileTree("$projectDir/SwiftCryptoKit")
            )
            outputs.files(
                fileTree("$projectDir/build/${conf}-${sdk}")
            )
        }
    }
}

tasks.create<Delete>("clean") {
    group = "build"

    delete("$projectDir/build")
}
