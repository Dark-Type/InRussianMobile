import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
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
                export(libs.ktor.client.darwin)
                export(libs.resources)

            }
        }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.serialization.core)
                api(libs.ktor.client.serialization)
                api(libs.resources)
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
        val androidMain by getting {
            dependencies {
                implementation(libs.androidx.security.crypto)
                implementation("androidx.datastore:datastore-preferences:1.0.0")
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
multiplatformResources {
    resourcesPackage.set("org.example.library")
    resourcesClassName.set("SharedRes")
   // resourcesVisibility.set(MRVisibility.Internal)
    iosBaseLocalizationRegion.set("en")
    iosMinimalDeploymentTarget.set("11.0")
}
