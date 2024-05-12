package request

import kotlinx.serialization.Serializable

@Serializable
data class PostRequest(
    val topic: String? = null,
    val message: String? = null
)
