package mai_onsyn.trisona.core.network.ncme.base

import io.ktor.client.HttpClient
import mai_onsyn.trisona.core.network.ncme.api.MusicApi

data class NCMApi(
    val client: HttpClient,
    val apiService: NCMApiService = NCMApiService(client),


    val music: MusicApi = MusicApi(apiService)
)