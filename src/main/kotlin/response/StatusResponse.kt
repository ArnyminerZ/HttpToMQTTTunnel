package response

import kotlinx.serialization.Serializable

@Serializable
data class StatusResponse(
    val uptime: Int
)
