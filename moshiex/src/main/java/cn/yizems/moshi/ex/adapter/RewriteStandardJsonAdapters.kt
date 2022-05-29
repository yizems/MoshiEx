package cn.yizems.moshi.ex.adapter

import cn.yizems.moshi.ex.util.hasFractional
import com.squareup.moshi.*
import com.squareup.moshi.internal.Util.NO_ANNOTATIONS
import com.squareup.moshi.internal.markNotNull
import java.lang.reflect.Type

class RewriteStandardJsonAdapters : JsonAdapter.Factory {
    /**
     * Attempts to create an adapter for `type` annotated with `annotations`. This
     * returns the adapter if one was created, or null if this factory isn't capable of creating
     * such an adapter.
     *
     *
     * Implementations may use [Moshi.adapter] to compose adapters of other types, or
     * [Moshi.nextAdapter] to delegate to the underlying adapter of the same type.
     */
    override fun create(
        type: Type,
        annotations: MutableSet<out Annotation>,
        moshi: Moshi
    ): JsonAdapter<*>? {
        if (annotations.isNotEmpty()) return null

        if (type === Boolean::class.javaPrimitiveType) return BOOLEAN_JSON_ADAPTER
        if (type === Boolean::class.javaObjectType) return BOOLEAN_JSON_ADAPTER.nullSafe()
        if (type === Double::class.javaPrimitiveType) return DOUBLE_JSON_ADAPTER
        if (type === Float::class.javaPrimitiveType) return FLOAT_JSON_ADAPTER
        if (type === Double::class.javaObjectType) return DOUBLE_JSON_ADAPTER.nullSafe()
        if (type === Float::class.javaObjectType) return FLOAT_JSON_ADAPTER.nullSafe()

        if (type === Any::class.java) return ObjectJsonAdapter(moshi).nullSafe()


        return null

    }


    private val BOOLEAN_JSON_ADAPTER: JsonAdapter<Boolean> = object : JsonAdapter<Boolean>() {
        override fun fromJson(reader: JsonReader): Boolean {
            return try {
                reader.nextBoolean()
            } catch (e: JsonDataException) {
                try {
                    val ret = reader.nextInt()
                    ret == 1
                } catch (e1: JsonDataException) {
                    try {
                        val readStr = reader.nextString()
                        readStr.equals("true", true)
                    } catch (e2: JsonDataException) {
                        throw JsonDataException(e.message + ",and not int,string, can not auto cast")
                    }
                }
            }
        }

        override fun toJson(writer: JsonWriter, value: Boolean?) {
            markNotNull(value)
            writer.value(value)
        }

        override fun toString() = "JsonAdapter(Boolean)"
    }

    private val DOUBLE_JSON_ADAPTER: JsonAdapter<Double> = object : JsonAdapter<Double>() {
        override fun fromJson(reader: JsonReader): Double {
            return reader.nextDouble()
        }

        override fun toJson(writer: JsonWriter, value: Double?) {
            markNotNull(value)
            if (!value.hasFractional()) {
                writer.value(value.toLong())
            } else {
                writer.value(value)
            }
        }

        override fun toString() = "JsonAdapter(Double)"
    }

    private val FLOAT_JSON_ADAPTER: JsonAdapter<Float> = object : JsonAdapter<Float>() {

        override fun fromJson(reader: JsonReader): Float {
            return reader.nextDouble().toFloat()
        }

        override fun toJson(writer: JsonWriter, value: Float?) {
            if (value == null) {
                throw NullPointerException()
            }
            val d = value.toDouble()
            if (!d.hasFractional()) {
                d.toLong().let {
                    writer.value(it)
                }
            } else {
                writer.value(value)
            }
        }

        override fun toString() = "JsonAdapter(Float)"
    }


    /**
     * This adapter is used when the declared type is [Any]. Typically the runtime
     * type is something else, and when encoding JSON this delegates to the runtime type's adapter.
     * For decoding (where there is no runtime type to inspect), this uses maps and lists.
     *
     * This adapter needs a Moshi instance to look up the appropriate adapter for runtime types as
     * they are encountered.
     */
    internal class ObjectJsonAdapter(private val moshi: Moshi) : JsonAdapter<Any>() {
        private val listJsonAdapter: JsonAdapter<List<*>> = moshi.adapter(List::class.java)
        private val mapAdapter: JsonAdapter<Map<*, *>> = moshi.adapter(Map::class.java)
        private val stringAdapter: JsonAdapter<String> = moshi.adapter(String::class.java)
        private val doubleAdapter: JsonAdapter<Double> = moshi.adapter(Double::class.java)
        private val booleanAdapter: JsonAdapter<Boolean> = moshi.adapter(Boolean::class.java)

        override fun fromJson(reader: JsonReader): Any? {
            return when (reader.peek()) {
                JsonReader.Token.BEGIN_ARRAY -> listJsonAdapter.fromJson(reader)
                JsonReader.Token.BEGIN_OBJECT -> mapAdapter.fromJson(reader)
                JsonReader.Token.STRING -> stringAdapter.fromJson(reader)
                JsonReader.Token.NUMBER -> {
                    val ret = doubleAdapter.fromJson(reader) ?: return null
                    if (ret.hasFractional()) {
                        return ret
                    } else {
                        ret.toLong()
                    }
                }
                JsonReader.Token.BOOLEAN -> booleanAdapter.fromJson(reader)
                JsonReader.Token.NULL -> reader.nextNull()
                else -> throw IllegalStateException(
                    "Expected a value but was ${reader.peek()} at path ${reader.path}"
                )
            }
        }

        override fun toJson(writer: JsonWriter, value: Any?) {
            markNotNull(value)
            val valueClass: Class<*> = value.javaClass
            if (valueClass == Any::class.java) {
                // Don't recurse infinitely when the runtime type is also Object.class.
                writer.beginObject()
                writer.endObject()
            } else {
                moshi.adapter<Any>(toJsonType(valueClass), NO_ANNOTATIONS).toJson(writer, value)
            }
        }

        /**
         * Returns the type to look up a type adapter for when writing `value` to JSON. Without
         * this, attempts to emit standard types like `LinkedHashMap` would fail because Moshi doesn't
         * provide built-in adapters for implementation types. It knows how to **write**
         * those types, but lacks a mechanism to read them because it doesn't know how to find the
         * appropriate constructor.
         */
        private fun toJsonType(valueClass: Class<*>): Class<*> {
            if (Map::class.java.isAssignableFrom(valueClass)) return Map::class.java
            return if (Collection::class.java.isAssignableFrom(valueClass)) Collection::class.java else valueClass
        }

        override fun toString() = "JsonAdapter(Object)"
    }
}
