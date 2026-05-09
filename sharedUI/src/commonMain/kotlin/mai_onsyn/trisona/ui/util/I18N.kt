package mai_onsyn.trisona.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.alibaba.fastjson2.JSONObject
import kotlinx.coroutines.runBlocking
import mai_onsyn.trisona.core.log
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.readResourceBytes
import trisona.sharedui.generated.resources.Res


object I18NManager {
    private var entries by mutableStateOf<Map<String, String>>(emptyMap())

    private var currentLanguage by mutableStateOf("zh_CN")

    @OptIn(InternalResourceApi::class)
    fun setLanguage(langCode: String) {
        currentLanguage = langCode

        runBlocking {
            try {
                Res.readBytes("files/lang/$langCode.json").decodeToString().let {
                    val rawJson = JSONObject.parse(it)
                    entries = flattenJsonObject(rawJson)
                }
            } catch (e: Exception) {
                log.error("Failed to load language file: $langCode", e)
            }
        }
    }

    init {
        setLanguage("zh_CN")
    }

    fun get(key: String, default: String): String {
        return entries[key] ?: default
    }
}

@Composable
fun t(default: String, path: String): String {
    return I18NManager.get(path, default)
}

@Composable
fun t(path: String): String {
    return I18NManager.get(path, path)
}