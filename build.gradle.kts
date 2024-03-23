plugins {
    alias(libs.plugins.jvm)
    alias(libs.plugins.ktor)
}

group = "com.arnyminerz"
version = "0.0.1"

application {
    mainClass.set("com.arnyminerz.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.logback)

    testImplementation(libs.ktor.server.tests)
    testImplementation(libs.kotlin.testJunit)
}
