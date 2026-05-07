package mai_onsyn.trisona

import androidx.compose.runtime.mutableStateOf
import com.russhwolf.settings.PropertiesSettings
import com.russhwolf.settings.Settings
import java.io.File
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

object Config {
    private val settings: Settings by lazy {
        val configFile = File(System.getProperty("user.dir"), "config.properties")
        println(configFile.absolutePath)

        val props = java.util.Properties()
        if (configFile.exists()) {
            configFile.inputStream().use { props.load(it) }
        }

        PropertiesSettings(props) {
            configFile.outputStream().use { props.store(it, "App Settings") }
        }
    }
    var isDarkMode by settings.build(false, "ui_is_dark_mode")
    var backgroundTriangleSize by settings.build(120f, "ui_background_triangle_size")
    var animationBaseRate by settings.build(300, "animation_base_rate")
    var volume by settings.build(50, "player_volume")
    var playMode by settings.build(0, "player_play_mode")
}

inline fun <reified T> Settings.build(
    default: T,
    key: String
): ReadWriteProperty<Any?, T> {
    val state = mutableStateOf(default)
    return object : ReadWriteProperty<Any?, T> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): T {
            val propertyValue = when (T::class) {
                Boolean::class -> getBoolean(key, default as Boolean) as T
                String::class -> getString(key, default as String) as T
                Int::class -> getInt(key, default as Int) as T
                Float::class -> getFloat(key, default as Float) as T
                Long::class -> getLong(key, default as Long) as T
                else -> throw IllegalArgumentException("不支持的配置类型: ${T::class}")
            }
            state.value = propertyValue
            return state.value
        }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
            when (value) {
                is Boolean -> putBoolean(key, value)
                is String -> putString(key, value)
                is Int -> putInt(key, value)
                is Float -> putFloat(key, value)
                is Long -> putLong(key, value)
                else -> throw IllegalArgumentException("不支持的配置类型")
            }
            state.value = value
        }
    }
}