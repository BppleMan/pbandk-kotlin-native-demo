plugins {
    kotlin("multiplatform")
}

repositories {
    mavenCentral()
}

val pbandkVersion: String by project

kotlin {
    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Mac OS X" -> macosArm64("native")
        hostOs == "Linux" -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    nativeTarget.apply {
        binaries {
            executable {
                entryPoint = "main"
            }
        }
    }

    sourceSets {
        val nativeMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
                implementation("io.ktor:ktor-server-core:2.1.0")
                implementation("io.ktor:ktor-server-cio:2.1.0")
                implementation("io.ktor:ktor-server-websockets:2.1.0")
                implementation("pro.streem.pbandk:pbandk-runtime:$pbandkVersion")
            }
        }
        val nativeTest by getting
    }
}
