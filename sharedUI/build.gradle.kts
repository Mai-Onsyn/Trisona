plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.android.kmp.library)
}

repositories {
    maven { url = uri("https://maven.aliyun.com/repository/public") }
    maven { url = uri("https://maven.aliyun.com/repository/google") }
    mavenCentral()
    google()
}

kotlin {
    androidTarget() //We need the deprecated target to have working previews

    jvm()

    sourceSets {
        commonMain.dependencies {
            api(libs.compose.runtime)
            api(libs.compose.ui)
            api(libs.compose.foundation)
            api(libs.compose.resources)
            api(libs.compose.ui.tooling.preview)
            api(libs.compose.material3)

            implementation(project(":core"))

            implementation("com.russhwolf:multiplatform-settings-no-arg:1.1.1")
//            implementation("io.coil-kt.coil3:coil-compose:3.3.0")
//            implementation("io.coil-kt.coil3:coil-network-ktor3:3.3.0")

            implementation("org.apache.logging.log4j:log4j-api:2.25.3")
            implementation("org.apache.logging.log4j:log4j-core:2.25.3")
            implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.25.3")

            implementation("org.jetbrains.kotlin:kotlin-reflect:2.1.21")
            implementation("com.alibaba.fastjson2:fastjson2:2.0.61")
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.compose.ui.test)
        }

        androidMain.dependencies {
        }

        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
        }

    }

}

dependencies {
    debugImplementation(libs.compose.ui.tooling)
}


android {
    namespace = "mai_onsn.trisona"
    compileSdk = 36
    defaultConfig {
        minSdk = 23
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
