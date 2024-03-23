import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.jetbrains.annotations.VisibleForTesting
import plugins.installContentNegotiation
import plugins.installRouting

fun main() {
    prepareServer()

    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
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
