# Swift static library inside a Kotlin/Native lib

This Kotlin/Native for iOS project demonstrates how to use a **Swift static library** into a **Kotlin/Native KLib** in order to use it as a dependency in another _Kotlin/Multiplatform Mobile_ project.

## Points of interest

- The [SwiftCryptoKit directory](https://github.com/SalomonBrys/Demo-SwiftLib-in-KMMLib/tree/main/SwiftCryptoKit) contains an XCode project that generates a Swift static library:
    - All its Swift classes and methods are `public` and `@objc` annotated to be accessible from Objective-C & Kotlin.
    - An "Objective-C Bridging Header" is configured for Objective-C interoperability.
    - A "Run Script" build phase is added at the end of the build to export the Swift Objective-C header with the library.
    - A Gradle build script was added to enable the root Gradle project to compile the library with `xcodebuild`.
- The root directory contains a Gradle project that provides a Kotlin/Native wrapper around the Swift static library:
    - The [Gradle build script](https://github.com/SalomonBrys/Demo-SwiftLib-in-KMMLib/blob/main/build.gradle.kts) declares a C-Interop that depends on the Swift static library
    - The [interop def file](https://github.com/SalomonBrys/Demo-SwiftLib-in-KMMLib/blob/main/src/nativeInterop/cinterop/SwiftCryptoKit.def) defines the linker flags that needs to be used when linking with the library:
        - **`-ios_simulator_version_min 13.0.0` and `-iphoneos_version_min 13.0.0` are needed because Swift interoperability needs iOS 13.0 or newer**.
        - Swift library search path are also needed.
- The [demo-app directory](https://github.com/SalomonBrys/Demo-SwiftLib-in-KMMLib/tree/main/demo-app) is a very simple KMM project with a shared Kotlin/Multiplatform library, and an iOS app. It demonstrates that the library can be used as a simple dependency in the shared library.
    - Note that the app must target iOS 13.0 or newer.
    

## Why, oh why?

Apple is pushing its Swift language more and more for iOS development.
Some new native features that are added to the system are being made available only in Swift: this is the case for example of the new **CryptoKit** framework, which exposes some crypto algorithm that weren't available before, such as ChaChaPoly.
Therefore, if we want to use a hardware-optimised implementation of ChaChaPoly, we have no choice but use it in Swift.

While Kotlin/Native does not interface with Swift, both Swift & Kotlin/Native interop really well with Objective-C, so it is rather simple to create a small static library that exposes some Swift-only APIs (such as ChaChaPoly in CryptoKit) to Objective-C and therefore to Kotlin/Native.

This demos a potential Kotlin/Multiplatform Mobile **library** (KLib) named "KMCrypto" that provides an API for some cryptographic algorithm (such as ChaChaPoly) and uses the system's most efficient implementation for each target.
This library would then be used as a dependencies on multiple Kotlin/Multiplatfom Mobile application projects.
Because it is a demo, only the Kotlin/Native iOS & Swift parts are implemented.
A real library would need to provide an implementation for Android.

As Apple is releasing more and more features in Swift only, this use case is becoming more and more important.
