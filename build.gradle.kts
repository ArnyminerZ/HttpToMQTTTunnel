import io.ktor.plugin.features.DockerPortMapping
import io.ktor.plugin.features.DockerPortMappingProtocol

plugins {
    alias(libs.plugins.jvm)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.ktor)
}

/**
 * Get the version of the application from the version.txt file.
 */
fun readVersion(): String {
    val pkg = File(rootDir, "package")
    val file = File(pkg, "version.txt")
    return file.readText()
}

group = "com.arnyminerz.httptomqtttunnel"
version = readVersion()

application {
    mainClass.set("ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

ktor {
    docker {
        jreVersion.set(JavaVersion.VERSION_17)

        localImageName.set("http-to-mqtt-tunnel")
        imageTag.set(readVersion())

        portMappings.set(
            listOf(DockerPortMapping(80, 8080, DockerPortMappingProtocol.TCP))
        )

        externalRegistry.set(
            io.ktor.plugin.features.DockerImageRegistry.dockerHub(
                appName = provider { "http-to-mqtt-tunnel" },
                username = providers.environmentVariable("DOCKER_HUB_USERNAME"),
                password = providers.environmentVariable("DOCKER_HUB_PASSWORD")
            )
        )
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // JSON Serialization
    implementation(libs.kotlinx.serialization)

    // Ktor Backend
    implementation(libs.ktor.serialization.kotlinx)
    implementation(libs.ktor.server.contentNegotiation)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)

    // Logging
    implementation(libs.logback)

    testImplementation(libs.ktor.server.tests)
    testImplementation(libs.kotlin.testJunit)
}
