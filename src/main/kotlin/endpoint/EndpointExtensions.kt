package endpoint

import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post

fun Route.get(endpoint: Endpoint): Route = get(endpoint.path) { with(endpoint) { endpoint() } }

fun Route.post(endpoint: Endpoint): Route = post(endpoint.path) { with(endpoint) { endpoint() } }
