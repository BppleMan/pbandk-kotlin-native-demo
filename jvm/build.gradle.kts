import com.google.protobuf.gradle.generateProtoTasks
import com.google.protobuf.gradle.id
import com.google.protobuf.gradle.ofSourceSet
import com.google.protobuf.gradle.plugins
import com.google.protobuf.gradle.protobuf
import com.google.protobuf.gradle.protoc

plugins {
    kotlin("jvm")
    id("com.google.protobuf")
    java
}

repositories {
    mavenCentral()
}

val protobufVersion: String by project
val pbandkVersion: String by project

dependencies {
    compileOnly("pro.streem.pbandk:pbandk-runtime:$pbandkVersion")
    compileOnly("pro.streem.pbandk:protoc-gen-pbandk-lib:$pbandkVersion")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

val syncGenerate = tasks.create("syncGenerate", Sync::class) {
    from("$projectDir/src/main/pbandk/generate")
    destinationDir = File("$projectDir/../native/src/nativeMain/kotlin/generate")
}

protobuf {
    generatedFilesBaseDir = "$projectDir/src"
    protoc {
        artifact = "com.google.protobuf:protoc:$protobufVersion"
        path = "/opt/homebrew/bin/protoc"
    }
    plugins {
        id("pbandk") {
            artifact = "pro.streem.pbandk:protoc-gen-pbandk-jvm:$pbandkVersion:jvm8@jar"
        }
    }
    generateProtoTasks {
        ofSourceSet("main").forEach { task ->
            task.dependsOn(":generator:jar")
            task.builtins.removeIf { it.name.contains("java") }
            task.plugins {
                id("pbandk") {
                    option("kotlin_package=generate.pb")
                    option("log=debug")
                    option("kotlin_service_gen=${project(":generator").buildDir}/libs/generator.jar|com.bppleman.Generator")
                }
            }
            task.finalizedBy(syncGenerate)
        }
    }
}
