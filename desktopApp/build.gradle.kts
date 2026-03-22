import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.kotlin.jvm)
}

repositories {
//    mavenCentral()
}

dependencies {
    implementation(project(":sharedUI"))
    implementation("net.java.dev.jna:jna:5.14.0")
    implementation("net.java.dev.jna:jna-platform:5.14.0")
    implementation("org.jetbrains.runtime:jbr-api:1.10.0")
}

compose.desktop {
    application {
        mainClass = "mai_onsyn.trisona.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Trisona"
            packageVersion = "1.0.0"

            linux {
                iconFile.set(project.file("src/main/resources/LinuxIcon.png"))
            }
            windows {
                iconFile.set(project.file("src/main/resources/WindowsIcon.ico"))
            }
            macOS {
                iconFile.set(project.file("src/main/resources/MacosIcon.icns"))
                bundleID = "mai_onsn.trisona.desktopApp"
            }
        }

        jvmArgs += listOf(
//            "-Dfile.encoding=UTF-8",
            "--add-opens=java.desktop/java.awt=ALL-UNNAMED",
            "--add-opens=java.desktop/sun.awt=ALL-UNNAMED",
            "--add-opens=java.desktop/sun.awt.windows=ALL-UNNAMED",
            "--enable-native-access=ALL-UNNAMED",
            "-Dfile.encoding=UTF-8",
            "-Dsun.stdout.encoding=UTF-8",
            "-Dsun.stderr.encoding=UTF-8"
        )
    }
}
