package cn.yizems.moshi.ex

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Types
import java.lang.reflect.Type


/**
 * to JsonAdapter
 * @param type 泛型
 */
fun <T> Class<T>.fromJson(str: String, lenient: Boolean = false): T? {
    return toAdapter(lenient).fromJson(str)
}

@Suppress("CheckResult")
fun <T> Type.fromJson(str: String, lenient: Boolean = false): T? {
    return toAdapter<T>().apply {
        if (lenient) lenient()
    }.fromJson(str)
}

/**
 * to type: 添加泛型参数
 * @param type 泛型
 */
fun Type.newParameterizedType(vararg type: Type): Type {
    if (type.isNotEmpty()) {
        return Types.newParameterizedType(this, *type)
    }
    return this
}

@Suppress("CheckResult")
fun <T> Class<T>.toAdapter(lenient: Boolean = false): JsonAdapter<T> {
    return moshiInstances.adapter(this)
        .apply {
            if (lenient) {
                this.lenient()
            }
        }
}

@Suppress("CheckResult")
fun <T> Type.toAdapter(vararg type: Type): JsonAdapter<T> {
    if (type.isNotEmpty()) {
        return moshiInstances.adapter(Types.newParameterizedType(this, *type))
    }
    return moshiInstances.adapter<T>(this)
}


/**
 * 转换为字符串
 */
@Suppress("CheckResult")
inline fun <reified T> T.toJsonString(serializeNulls: Boolean = false): String {
    return T::class.java.toAdapter()
        .apply {
            if (serializeNulls) {
                this.serializeNulls()
            }
        }
        .toJson(this)
}

/**
 * 转换为别的对象
 */
inline fun <reified T, R> T.toOtherBean(clz: Class<R>, lenient: Boolean = false): R? {
    return clz
        .toAdapter(lenient)
        .fromJson(
            T::class.java
                .toAdapter()
                .toJson(this)
        )
}

/**
 * 转换为别的对象
 */
inline fun <reified T, R> T.toOtherBean(type: Type): R? {
    return type
        .toAdapter<R>()
        .fromJson(
            T::class.java
                .toAdapter()
                .toJson(this)
        )
}

fun <T> String.toClass(clz: Class<T>, lenient: Boolean = false): T? {
    return clz.toAdapter(lenient).fromJson(this)
}

fun <T> String.toClassList(clz: Class<T>, lenient: Boolean = false): List<T>? {
    return List::class.java
        .newParameterizedType(clz)
        .fromJson<List<T>>(this, lenient)
}

fun <T, R> String.toMap(key: Class<T>, value: Class<R>, lenient: Boolean = false): Map<T, R?>? {
    return Map::class.java
        .newParameterizedType(key, value)
        .fromJson<Map<T, R?>>(this, lenient)
}

fun <T> String.toMap(value: Class<T>, lenient: Boolean = false): Map<String, T?>? {
    return toMap(String::class.java, value, lenient)
}

@Suppress("CheckResult")
fun <T> String.toType(type: Type, lenient: Boolean = false): T? {
    return type.toAdapter<T>().apply {
        if (lenient) {
            this.lenient()
        }
    }.fromJson(this)
}
