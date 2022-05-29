package cn.yizems.moshi.ex

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Types
import java.lang.reflect.Type


/**
 * to JsonAdapter
 * @param type 泛型
 */
fun <T> Class<T>.fromJson(str: String): T? {
    return toAdapter().fromJson(str)
}

fun <T> Type.fromJson(str: String): T? {
    return toAdapter<T>().fromJson(str)
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

fun <T> Class<T>.toAdapter(): JsonAdapter<T> {
    return moshiInstances.adapter(this)
}

fun <T> Type.toAdapter(vararg type: Type): JsonAdapter<T> {
    if (type.isNotEmpty()) {
        return moshiInstances.adapter(Types.newParameterizedType(this, *type))
    }
    return moshiInstances.adapter(this)
}


/**
 * 转换为字符串
 */
inline fun <reified T> T.toJsonString(): String {
    return T::class.java.toAdapter()
        .toJson(this)
}

/**
 * 转换为别的对象
 */
inline fun <reified T, R> T.toOtherBean(clz: Class<R>): R? {
    return clz
        .toAdapter()
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

fun <T> String.toClass(clz: Class<T>): T? {
    return clz.toAdapter().fromJson(this)
}

fun <T> String.toClassList(clz: Class<T>): List<T>? {
    return List::class.java
        .newParameterizedType(clz)
        .fromJson<List<T>>(this)
}

fun <T, R> String.toMap(key: Class<T>, value: Class<R>): Map<T, R?>? {
    return Map::class.java
        .newParameterizedType(key, value)
        .fromJson<Map<T, R?>>(this)
}

fun <T> String.toMap(value: Class<T>): Map<String, T?>? {
    return toMap(String::class.java, value)
}

fun <T> String.toType(type: Type): T? {
    return type.toAdapter<T>().fromJson(this)
}
