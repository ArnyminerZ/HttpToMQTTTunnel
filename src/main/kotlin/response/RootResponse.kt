package response

import Environment
import kotlinx.serialization.Serializable

@Serializable
data class RootResponse(
    val version: String = Environment.version
)
