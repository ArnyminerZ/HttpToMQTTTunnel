import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.jetbrains.annotations.VisibleForTesting
import plugins.installContentNegotiation
import plugins.installRouting
import server.MQTT

fun main() {
    prepareServer()

    MQTT.init()
    MQTT.connect()

    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)

    MQTT.disconnect()
}

@VisibleForTesting
fun prepareServer() {
    // Read the version from the version.txt file
    Environment.readVersionFile()
}

private fun Application.module() {
    installContentNegotiation()
    installRouting()
}
