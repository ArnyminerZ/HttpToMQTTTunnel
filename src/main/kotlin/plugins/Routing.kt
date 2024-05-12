package plugins

import endpoint.PostEndpoint
import endpoint.RootEndpoint
import endpoint.StatusEndpoint
import endpoint.get
import endpoint.post
import io.ktor.server.application.Application
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.post
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
    get(RootEndpoint)
    // allows pinging MQTT status
    get(StatusEndpoint)
    // MQTT publish requests
    post(PostEndpoint)
}
