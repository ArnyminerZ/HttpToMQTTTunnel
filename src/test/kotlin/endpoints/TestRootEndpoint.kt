package endpoints

import Environment
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlin.test.Test
import kotlin.test.assertEquals
import model.TestServerModel
import response.RootResponse

class TestRootEndpoint : TestServerModel() {
    @Test
    fun testRootEndpoint() = testServer { client ->
        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
            val response: RootResponse = body()
            assertEquals(Environment.version, response.version)
        }
    }
}
