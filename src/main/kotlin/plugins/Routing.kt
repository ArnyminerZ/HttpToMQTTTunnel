package plugins

import endpoint.RootEndpoint
import endpoint.StatusEndpoint
import io.ktor.server.application.Application
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import org.jetbrains.annotations.VisibleForTesting

fun Application.installRouting() {
    routing {
        configureRouting()
    }
}

@VisibleForTesting
fun Routing.configureRouting() {
    // root endpoint, provides general information, and serves as ping
    get("/") { with(RootEndpoint) { endpoint() } }
    get("/status") { with(StatusEndpoint) { endpoint() } }
}
