package mai_onsyn.trisona.core.network.ncme.base

import com.alibaba.fastjson2.JSON
import com.alibaba.fastjson2.JSONObject
import com.alibaba.fastjson2.JSONPath
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
//import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.prepareGet
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsBytes
import io.ktor.client.statement.bodyAsChannel
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.readRawBytes
import io.ktor.utils.io.jvm.javaio.toInputStream
import mai_onsyn.trisona.core.log
import okhttp3.Protocol
import java.io.Closeable
import java.io.InputStream

class NCMApiService(val client: HttpClient) {

    suspend fun requestJson(path: String, params: Map<String, String>): JSONObject {
        val response: HttpResponse = client.get(path) {
            url {
                params.forEach { (key, value) ->
                    parameters.append(key, value)
                }
                if (!params.containsKey("randomCNIP")) {
                    parameters.append("randomCNIP", "true")
                }
            }
        }
        val fullUrl = response.call.request.url.toString()
        val text = response.bodyAsText(Charsets.UTF_8)
        log.debug("Request: $fullUrl")
        log.debug("Response: $text")
        return JSON.parseObject(text)
    }

    suspend fun requestBody(path: String, params: Map<String, String>): ApiResponse {
        val statement = client.prepareGet(path) {
            url {
                params.forEach { (key, value) ->
                    parameters.append(key, value)
                }
                if (!params.containsKey("randomCNIP")) {
                    parameters.append("randomCNIP", "true")
                }
            }
        }

        val response = statement.execute()
        return ApiResponse(response)
    }
}

class ApiResponse(private val response: HttpResponse) : Closeable {
    suspend fun getInputStream(): InputStream {
        return response.bodyAsChannel().toInputStream()
    }

    suspend fun getBodyText(): String {
        return response.bodyAsText()
    }

    suspend fun <T> parseJson(clazz: Class<T>): T {
        return JSONObject.parseObject(getBodyText(), clazz)
    }

    fun getStatusCode(): Int = response.status.value

    override fun close() {}
}