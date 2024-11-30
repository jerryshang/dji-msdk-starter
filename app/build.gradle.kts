plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.kotlin.compose)
}

android {
  namespace = "dev.tireless.moo"
  compileSdk = 35

  defaultConfig {
    applicationId = "dev.tireless.moo"
    minSdk = 25
    targetSdk = 35
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    ndk {
      abiFilters.retainAll(setOf("arm64-v8a"))
    }
    packaging {
      jniLibs {
        useLegacyPackaging = true
        pickFirsts +=
          setOf(
            "lib/arm64-v8a/libc++_shared.so",
          )
        keepDebugSymbols +=
          setOf(
            // dji msdk
            "*/*/libconstants.so",
            "*/*/libdji_innertools.so",
            "*/*/libdjibase.so",
            "*/*/libDJICSDKCommon.so",
            "*/*/libDJIFlySafeCore-CSDK.so",
            "*/*/libdjifs_jni-CSDK.so",
            "*/*/libDJIRegister.so",
            "*/*/libdjisdk_jni.so",
            "*/*/libDJIUpgradeCore.so",
            "*/*/libDJIUpgradeJNI.so",
            "*/*/libDJIWaypointV2Core-CSDK.so",
            "*/*/libdjiwpv2-CSDK.so",
            "*/*/libFlightRecordEngine.so",
            "*/*/libvideo-framing.so",
            "*/*/libwaes.so",
            "*/*/libagora-rtsa-sdk.so",
            "*/*/libc++.so",
            "*/*/libc++_shared.so",
            "*/*/libmrtc_28181.so",
            "*/*/libmrtc_agora.so",
            "*/*/libmrtc_core.so",
            "*/*/libmrtc_core_jni.so",
            "*/*/libmrtc_data.so",
            "*/*/libmrtc_log.so",
            "*/*/libmrtc_onvif.so",
            "*/*/libmrtc_rtmp.so",
            "*/*/libmrtc_rtsp.so",
          )
      }
    }
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro",
      )
      signingConfig = signingConfigs.getByName("debug")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
  kotlinOptions {
    jvmTarget = "11"
  }
  buildFeatures {
    compose = true
  }
}

dependencies {
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.appcompat)
  implementation(libs.material)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.activity.compose)
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.ui)
  implementation(libs.androidx.ui.graphics)
  implementation(libs.androidx.ui.tooling.preview)
  implementation(libs.androidx.material3)
  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(platform(libs.androidx.compose.bom))
  androidTestImplementation(libs.androidx.ui.test.junit4)
  debugImplementation(libs.androidx.ui.tooling)

  implementation(libs.dji.sdk)
  compileOnly(libs.dji.sdk.provided)
  runtimeOnly(libs.dji.sdk.network)
}
