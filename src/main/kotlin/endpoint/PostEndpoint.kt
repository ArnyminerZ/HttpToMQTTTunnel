package endpoint

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.util.pipeline.PipelineContext
import request.PostRequest
import server.MQTT

object PostEndpoint : Endpoint("/post") {
    override suspend fun PipelineContext<Unit, ApplicationCall>.endpoint() {
        try {
            val body = call.receive<PostRequest>()
            MQTT.publish(body.topic, body.message)
            call.respond(HttpStatusCode.Accepted, "OK")
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, "${e::class.simpleName} :: ${e.message}")
        }
    }
}
