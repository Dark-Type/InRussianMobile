import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinSerialization)

}

kotlin {
    applyDefaultHierarchyTemplate() 

    // --- Android target ---
    androidTarget {
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

                implementation(libs.kotlinx.coroutines.core)
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }


    sourceSets.getByName("main") {
        manifest.srcFile("src/androidMain/AndroidManifest.xml")
    }
}

