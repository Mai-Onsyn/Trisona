package mai_onsyn.trisona.core.network.common

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.defaultRequest
import mai_onsyn.trisona.core.network.ncme.base.NCMApi
import okhttp3.Protocol

val client = HttpClient(OkHttp) {
    install(HttpTimeout) {
        requestTimeoutMillis = 30000
        connectTimeoutMillis = 30000
        socketTimeoutMillis = 30000
    }
    engine {
        config {
            followRedirects(true)
            protocols(listOf(Protocol.HTTP_2, Protocol.HTTP_1_1))
        }
    }

    defaultRequest {
        url("https://ncmapi.mai-onsyn.us.ci")
    }
}

val API = NCMApi(client)