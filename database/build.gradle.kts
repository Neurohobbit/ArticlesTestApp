import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.android.lint)
    alias(libs.plugins.kspCompose)
    alias(libs.plugins.room)
}

kotlin {

    androidLibrary {
        namespace = "neurohobbit.articles.database"
        compileSdk = 36
        minSdk = 24

        withHostTestBuilder {
        }

        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation(libs.room.runtime)
                implementation(libs.sqlite.bundled)
                api(libs.koin.core)
            }
        }

        androidMain {
            dependencies {
//                ksp("androidx.room:room-compiler:2.6.1")
                implementation(libs.room.runtime)
                implementation(libs.androidx.room.ktx)
            }
        }
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    add("kspCommonMainMetadata", libs.room.compiler)
    add("kspAndroid", "androidx.room:room-compiler:2.8.3")
    add("kspCommonMainMetadata", "androidx.room:room-compiler:2.8.3")
}
