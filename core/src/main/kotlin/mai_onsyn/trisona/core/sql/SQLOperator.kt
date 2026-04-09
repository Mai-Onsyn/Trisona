package mai_onsyn.trisona.core.sql

import com.alibaba.fastjson2.JSONArray
import com.alibaba.fastjson2.JSONObject

open class SQLOperator(val instance: SQLInstance) {
    protected fun insert(tableName: String, id: Long, column: String, value: Any) {
        instance.connection.prepareStatement(
            """
                insert into $tableName (id, $column) VALUES (?, ?)
                on conflict(id) do update set
                    $column = excluded.$column
            """.trimIndent()
        ).use {
            it.setLong(1, id)
            when (value) {
                is String -> it.setString(2, value)
                is Long -> it.setLong(2, value)
                is Int -> it.setInt(2, value)
                is Double -> it.setDouble(2, value)
                is Float -> it.setDouble(2, value.toDouble())
                is Boolean -> it.setInt(2, if (value) 1 else 0)
                is List<*> -> {
                    val list = JSONArray()
                    value.forEach { any -> list.add(any) }
                    it.setString(2, list.toJSONString())
                }
                is Map<*, *> -> {
                    val map = JSONObject()
                    value.forEach { (key, value) -> map[key.toString()] = value }
                    it.setString(2, map.toJSONString())
                }
                else -> throw IllegalArgumentException("Unsupported type: ${value::class}")
            }
            it.execute()
        }
    }

    protected fun exists(tableName: String, id: Long): Boolean {
//        if (id == -1L) return false;
        val sql = "select count(1) from $tableName where id = ?"

        return instance.connection.prepareStatement(sql).use {
            it.setLong(1, id)
            it.executeQuery().use { rs ->
                if (rs.next()) {
                    rs.getInt(1) > 0
                } else {
                    false
                }
            }
        }
    }

    protected fun queryLong(tableName: String, id: Long, column: String): Long? = query<Long>(tableName, id, column)

    protected fun queryString(tableName: String, id: Long, column: String): String? = query<String>(tableName, id, column)

    protected fun queryInt(tableName: String, id: Long, column: String): Int? = query<Int>(tableName, id, column)

    protected fun queryDouble(tableName: String, id: Long, column: String): Double? = query<Double>(tableName, id, column)

    protected fun queryBoolean(tableName: String, id: Long, column: String): Boolean? = query<Boolean>(tableName, id, column)

    protected inline fun <reified T> query(tableName: String, id: Long, column: String): T? {
        val sql = "select $column from $tableName where id = ? limit 1"

        return instance.connection.prepareStatement(sql).use {
            it.setLong(1, id)
            it.executeQuery().use { rs ->
                if (rs.next()) {
                    val result = when (T::class) {
                        Long::class -> {
                            val v = rs.getLong(column)
                            if (rs.wasNull()) null else v
                        }
                        Int::class -> {
                            val v = rs.getInt(column)
                            if (rs.wasNull()) null else v
                        }
                        Boolean::class -> {
                            val v = rs.getInt(column)
                            if (rs.wasNull()) null else (v == 1)
                        }
                        Double::class -> {
                            val v = rs.getDouble(column)
                            if (rs.wasNull()) null else v
                        }
                        String::class -> rs.getString(column)
                        else -> null
                    }
                    result as? T
                } else null
            }
        }
    }
}