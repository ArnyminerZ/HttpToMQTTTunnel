package endpoint

import io.ktor.server.application.ApplicationCall
import io.ktor.util.pipeline.PipelineContext

abstract class Endpoint {
    abstract suspend fun PipelineContext<Unit, ApplicationCall>.endpoint()
}
