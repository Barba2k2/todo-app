plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.kotlin.serialization)
  alias(libs.plugins.android.library)
}

kotlin {
  androidTarget()
  iosX64()
  iosArm64()
  iosSimulatorArm64()

  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation(project(":domain"))
        implementation(libs.coroutines.core)
        implementation(libs.serialization.json)
        implementation(libs.datetime)
        implementation(libs.ktor.client.core)
        implementation(libs.ktor.content.negotiation)
        implementation(libs.ktor.serialization.json)
      }
    }
    val commonTest by getting

    val androidMain by getting {
      dependencies {
        implementation(libs.ktor.client.okhttp)
        implementation(libs.security.crypto)
      }
    }
    val iosMain by creating {
      dependsOn(commonMain)
      dependencies {
        implementation(libs.ktor.client.darwin)
      }
    }
    val iosX64Main by getting { dependsOn(iosMain) }
    val iosArm64Main by getting { dependsOn(iosMain) }
    val iosSimulatorArm64Main by getting { dependsOn(iosMain) }
  }
}

android {
  namespace = "com.todoapp.data"
  compileSdk = 35
  defaultConfig {
    minSdk = 26
  }
}
