package endpoint

import io.ktor.server.application.ApplicationCall
import io.ktor.util.pipeline.PipelineContext

abstract class Endpoint(
    val path: String
) {
    abstract suspend fun PipelineContext<Unit, ApplicationCall>.endpoint()
}
