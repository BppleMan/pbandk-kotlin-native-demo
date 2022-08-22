pluginManagement {
    val kotlinVersion: String by settings
    val protobufVersion: String by settings

    plugins {
        kotlin("multiplatform") version kotlinVersion
        kotlin("jvm") version kotlinVersion
        id("com.google.protobuf") version protobufVersion
    }
}

rootProject.name = "pbandk-kotlin-native-demo"
include(":native", ":jvm", ":generator")
