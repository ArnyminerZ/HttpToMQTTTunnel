package endpoint

import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.util.pipeline.PipelineContext
import response.RootResponse

object RootEndpoint : Endpoint("/") {
    override suspend fun PipelineContext<Unit, ApplicationCall>.endpoint() {
        call.respond(RootResponse())
    }
}
