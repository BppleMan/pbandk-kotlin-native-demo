plugins {
    kotlin("jvm")
}

repositories {
    mavenCentral()
}

val pbandkVersion: String by project

dependencies {
    implementation("pro.streem.pbandk:protoc-gen-pbandk-lib:$pbandkVersion")
}
