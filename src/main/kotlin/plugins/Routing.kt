package plugins

import endpoint.RootEndpoint
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.annotations.VisibleForTesting
import response.RootResponse

fun Application.installRouting() {
    routing {
        configureRouting()
    }
}

@VisibleForTesting
fun Routing.configureRouting() {
    // root endpoint, provides general information, and serves as ping
    get("/") { with(RootEndpoint) { endpoint() } }
}
