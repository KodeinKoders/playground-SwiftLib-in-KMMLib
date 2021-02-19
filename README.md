# HELP!

This Kotlin/Native for iOS project demonstrates a use case where we need to use a **Swift static library** into a **Kotlin/Native KLib** in order to use it as a dependency in another _Kotlin/Multiplatform Mobile project_.

At the moment, **IT DOES NOT WORK**, which is why I require your help!


## State of the project

### What works

- Compilation of the Swift static library with Objective-C bridging
- C/Interop of the Objective-C generated header to Kotlin/Native
- Compilation of the Kotlin wrapper
- Linking of the test executable

### What does NOT work

- Execution of the test executable
- Use of the library inside a KMM project (the framework compiles, but the app execution fails at startup)

The error, in both cases is:

```
dyld: Library not loaded: @rpath/libswiftCore.dylib
  Referenced from: [path-of-the-project]/build/bin/ios/debugTest/test.kexe
  Reason: image not found
```

### How to reproduce

Checkout the repository and execute the Gradle task iosTest: `./gradlew iosTest`.

### What I think is going on

Swift 5 stabilized its ABI and the Swift runtime is not included statically at link-time, it is provided by the system.
However, XCode and its linker (ld64) perform some magic to the executable or framework when it detects Swift objects (as explained by this [very good article](https://milen.me/writings/apple-link-magic-swift-runtime/)).

Kotlin/Native performs no such magic and the generated executable or framework is not configured to find the Swift 5 runtime in the target system.
I suppose there are some linker flags we could pass to the Kotlin/Native linker to fix that. I have found the flags that make the executable **link** correctly (see the `src/nativeInterop/cinterop/SwiftCryptoKit.def` file), but not **execute**.


## Why, oh why?

Apple is pushing its Swift language more and more for iOS development.
Some new native features that are added to the system are being made available only in Swift: this is the case for example of the new **CryptoKit** framework, which exposes some crypto algorithm that weren't available before, such as ChaChaPoly.
Therefore, if we want to use a hardware-optimised implementation of ChaChaPoly, we have no choice but use it in Swift.

While Kotlin/Native does not interface with Swift, both Swift & Kotlin/Native interop really well with Objective-C, so it is rather simple to create a small static library that exposes some Swift-only APIs (such as ChaChaPoly in CryptoKit) to Objective-C and therefore to Kotlin/Native.

What we want to achieve is a Kotlin/Multiplatform Mobile **library** (KLib) named "KMCrypto" that provides an API for some cryptographic algorithm (such as ChaChaPoly) and uses the system's most efficient implementation for each target.
This library would then be used as a dependencies on multiple Kotlin/Multiplatfom Mobile application projects.

As Apple is releasing more and more features in Swift only, this use case is becoming more and more important.
