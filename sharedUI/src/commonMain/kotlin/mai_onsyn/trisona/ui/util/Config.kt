package mai_onsyn.trisona.ui.util

import androidx.compose.runtime.mutableStateOf
import com.alibaba.fastjson2.JSON
import com.alibaba.fastjson2.JSONObject
import com.alibaba.fastjson2.JSONPath
import com.alibaba.fastjson2.JSONWriter
import mai_onsyn.trisona.core.TrisonaKotlinInterface.player
import mai_onsyn.trisona.core.play.PlayQueue
import java.io.File
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

object Config {
    private val configFile = File(System.getProperty("user.dir"), "config.json")

    // 初始化 JSON 对象
    private val jsonObject: JSONObject = if (configFile.exists()) {
        try {
            JSON.parseObject(configFile.readText())
        } catch (e: Exception) {
            JSONObject()
        }
    } else {
        JSONObject()
    }

    // 持久化函数
    private fun save() {
        configFile.writeText(
            JSON.toJSONString(jsonObject, JSONWriter.Feature.PrettyFormat)
        )
    }

    // 暴露给委托使用的读写接口
    fun <T> access(path: String, default: T): Pair<T, (T) -> Unit> {
        val value = JSONPath.eval(jsonObject, "$.$path") as? T?: default

        val updater = { newValue: T ->
            JSONPath.set(jsonObject, "$.$path", newValue)
            save()
        }

        return value to updater
    }

    var isDarkMode by build(false, "ui.theme.is_dark_mode")
    var playerVolume by build(0.4f, "player.volume", onChange = { player.volume = (it * 100).toInt() })
    var playerPlayMode by build(PlayQueue.PlayMode.SEQUENCE.ordinal, "player.play_mode", onChange = { player.setPlayMode(it) })
    var animationBaseRate by build(300, "ui.animation.base_rate")
}

inline fun <reified T> build(
    default: T,
    path: String,
    crossinline onChange: (T) -> Unit = {}
): ReadWriteProperty<Any?, T> {
    val (initialValue, updateFn) = Config.access(path, default)
    val state = mutableStateOf(initialValue)

    return object : ReadWriteProperty<Any?, T> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): T {
            return state.value
        }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
            if (state.value != value) {
                state.value = value
                updateFn(value)
                onChange(value)
            }
        }
    }
}