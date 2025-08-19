import dev.icerock.gradle.MRVisibility
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
    id("dev.icerock.mobile.multiplatform-resources")
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

            }
        }

    sourceSets {
        val commonMain by getting {
            dependencies {

                api(libs.decompose)
                api(libs.essenty.lifecycle)

                api(libs.resources)
                api(libs.resources.compose)
                api(libs.kotlinx.datetime)
                implementation(libs.kermit)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.decompose)
                implementation(compose.components.resources)
                implementation(libs.mvikotlin)
                implementation(libs.mvikotlin.main)
                implementation(libs.mvikotlin.coroutines)
                implementation(libs.koin.core)
                implementation(libs.kotlinx.serialization.json)
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
multiplatformResources {
    resourcesPackage.set("org.example.library") // required
    resourcesClassName.set("SharedRes") // optional, default MR
    resourcesVisibility.set(MRVisibility.Internal) // optional, default Public
    iosBaseLocalizationRegion.set("en") // optional, default "en"
    iosMinimalDeploymentTarget.set("11.0")
}