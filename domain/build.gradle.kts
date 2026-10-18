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
        implementation(libs.coroutines.core)
        implementation(libs.datetime)
      }
    }
    val commonTest by getting

    val androidMain by getting
    val iosMain by creating {
      dependsOn(commonMain)
    }
    val iosX64Main by getting { dependsOn(iosMain) }
    val iosArm64Main by getting { dependsOn(iosMain) }
    val iosSimulatorArm64Main by getting { dependsOn(iosMain) }
  }
}

android {
  namespace = "com.todoapp.domain"
  compileSdk = 35
  defaultConfig {
    minSdk = 26
  }
}
