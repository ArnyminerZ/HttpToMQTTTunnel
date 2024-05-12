package response

import kotlinx.serialization.Serializable

@Serializable
data class StatusResponse(
    val connected: Boolean,
    val uptime: Int
)
