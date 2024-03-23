package model

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import plugins.configureContentNegotiation
import plugins.configureRouting

abstract class TestServerModel : TestModel() {
    protected fun testServer(
        block: suspend ApplicationTestBuilder.(client: HttpClient) -> Unit
    ) = testApplication {
        install(io.ktor.server.plugins.contentnegotiation.ContentNegotiation) { configureContentNegotiation() }
        routing { configureRouting() }

        val client = createClient {
            install(ContentNegotiation) { json() }
        }

        block(client)
    }
}
