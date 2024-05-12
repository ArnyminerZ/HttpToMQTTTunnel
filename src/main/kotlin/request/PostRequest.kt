package request

import kotlinx.serialization.Serializable

@Serializable
data class PostRequest(
    val topic: String,
    val message: String
)
