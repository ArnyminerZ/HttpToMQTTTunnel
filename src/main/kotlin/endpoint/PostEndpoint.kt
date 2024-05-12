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
            val (topic, message) = body

            if (topic == null || message == null) {
                call.respond(HttpStatusCode.BadRequest, "Missing 'topic' or 'message' in the request body")
                return
            }

            MQTT.publish(topic, message)
            call.respond(HttpStatusCode.Accepted, "OK")
        } catch (e: Exception) {
            e.printStackTrace(System.err)
            call.respond(HttpStatusCode.InternalServerError, "${e::class.simpleName} :: ${e.message}")
        }
    }
}
