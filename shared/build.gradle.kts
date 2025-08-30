import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val ktor_version = "3.1.3"
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinSerialization)
//    id("dev.icerock.mobile.multiplatform-resources")
}

kotlin {
    applyDefaultHierarchyTemplate()

    // --- Android target ---
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    // --- iOS targets ---
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    )
        .forEach { iosTarget ->
            iosTarget.binaries.framework {
                baseName = "Shared"

                export(libs.decompose)
                export(libs.essenty.lifecycle)
                export(libs.ktor.client.darwin)
//                export(libs.resources)
//                export("dev.icerock.moko:graphics:0.10.0")

            }
        }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.serialization.core)
                api(libs.ktor.client.serialization)

                api(libs.kotlinx.datetime)
                api(libs.decompose)
                api(libs.essenty.lifecycle)

                implementation(libs.kermit)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.decompose)
                implementation(libs.mvikotlin)
                implementation(libs.mvikotlin.main)
                implementation(libs.mvikotlin.coroutines)
                implementation(libs.koin.core)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.kotlinx.json)
            }
        }
        val iosMain by getting {
            dependencies {
                api(libs.ktor.client.darwin)
            }
        }


        iosMain {
            dependencies {
                api(libs.ktor.client.darwin)
            }
        }
    }
}

android {
    namespace = "com.example.inrussian.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    sourceSets {
        named("main") {
            kotlin.srcDir("src/androidMain/kotlin")
            res.srcDir("src/androidMain/res")
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
        }

        named("androidTest") {
            kotlin.srcDir("src/androidTest/kotlin")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
//multiplatformResources {
//    resourcesPackage.set("org.example.library") // required
//    resourcesClassName.set("SharedRes") // optional, default MR
//    resourcesVisibility.set(MRVisibility.Internal) // optional, default Public
//    iosBaseLocalizationRegion.set("en") // optional, default "en"
//    iosMinimalDeploymentTarget.set("11.0")
//}