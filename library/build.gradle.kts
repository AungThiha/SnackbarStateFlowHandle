import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    id("com.vanniktech.maven.publish") version "0.32.0"
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "SnackbarStateFlowHandle"
            isStatic = true
        }
    }

    jvm("desktop")

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        outputModuleName = "SnackbarStateFlowHandle"
        browser {
            val rootDirPath = project.rootDir.path
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                outputFileName = "snackbarStateFlowHandle.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(rootDirPath)
                        add(projectDirPath)
                    }
                }
            }
        }
        binaries.executable()
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.androidx.lifecycle.runtimeCompose)
                implementation(compose.runtime)
                implementation(compose.material3)
                implementation(compose.components.resources)
            }
        }
        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.kotlinx.coroutines.test)
            }
        }
    }
}

android {
    namespace = "io.github.aungthiha.snackbar"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    signAllPublications()

    coordinates(
        groupId = "io.github.aungthiha",
        artifactId = "snackbar-stateflow-handle",
        version = "1.0.0"
    )

    pom {
        name.set("SnackbarStateFlowHandle")
        description.set("A lifecycle-aware Snackbar library that eliminates boilerplate and prevents missed/duplicated snackbars in KMP/CMP.")
        url.set("https://github.com/AungThiha/SnackbarStateFlowHandle")

        licenses {
            license {
                name.set("MIT License")
                url.set("https://github.com/AungThiha/SnackbarStateFlowHandle/blob/main/LICENSE")
            }
        }

        developers {
            developer {
                id.set("AungThiha")
                name.set("Aung Thiha")
                email.set("mr.aungthiha@gmail.com")
            }
        }

        scm {
            connection.set("scm:git:github.com/AungThiha/SnackbarStateFlowHandle.git")
            developerConnection.set("scm:git:ssh://github.com/AungThiha/SnackbarStateFlowHandle.git")
            url.set("https://github.com/AungThiha/SnackbarStateFlowHandle")
        }
    }
}
