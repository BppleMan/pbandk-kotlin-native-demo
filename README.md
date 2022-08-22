# pbandk-kotlin-native-demo

## introduction

This is a minimal demo for [streem/pbandk](https://github.com/streem/pbandk) with kotlin-native

We know that currently neither protobuf nor grpc have good support for kotlin-native

When I found pbandk. 

After learning about it, I made this repository for the minimal implementation of kotlin-native with protobuf and grpc service

Then you can use it with rsocket-kotlin or ktor to serve the network.

## Useage

Only

```
git clone https://github.com/BppleMan/pbandk-kotlin-native-demo.git
cd pbandk-kotlin-native-demo
./gradlew runDebugExecutableNative
```

Then you will got the output

```
Result: Hello, World!
```

---

## How To Develop

All the code is from streem/pbandk's example, just the original example is for kotlin-jvm, and after I retrofit it, it will work for kotlin-native.

This repository is built on gradle with kts and uses 3 submodules, all of which are declared in [settings.gradle.kts](settings.gradle.kts)

### jvm

Used to virtualize a jvm running environment for [google/protobuf-gradle-plugin](https://github.com/google/protobuf-gradle-plugin), and generate all the main code in this module

