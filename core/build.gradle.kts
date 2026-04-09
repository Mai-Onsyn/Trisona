plugins {
    id("java")
    kotlin("jvm") version "2.3.0"
}

group = "org.example"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation(kotlin("stdlib-jdk8"))
    implementation("io.ktor:ktor-client-core:3.4.1")
    implementation("io.ktor:ktor-client-cio:3.4.1")
    implementation("io.ktor:ktor-client-jvm:3.4.1")
    implementation("io.ktor:ktor-client-okhttp:3.4.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")

    implementation("org.apache.logging.log4j:log4j-api:2.25.3")
    implementation("org.apache.logging.log4j:log4j-core:2.25.3")
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl:2.25.3")

    implementation("javazoom:jlayer:1.0.1")
    implementation(files(layout.projectDirectory.file("libs/jflac-codec-1.3.6.jar")))
    implementation("net.jthink:jaudiotagger:3.0.1")
    implementation("com.alibaba.fastjson2:fastjson2:2.0.61")
    implementation("org.xerial:sqlite-jdbc:3.51.3.0")
}

tasks.test {
    useJUnitPlatform()

    jvmArgs(
        "--enable-native-access=ALL-UNNAMED"
    )
}
kotlin {
    jvmToolchain(21)
}