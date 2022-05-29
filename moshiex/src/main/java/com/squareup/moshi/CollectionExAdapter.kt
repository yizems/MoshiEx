/*
 * Copyright (C) 2014 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// copy from CollectionJsonAdapter
package com.squareup.moshi

import com.squareup.moshi.internal.markNotNull
import java.lang.reflect.Type

abstract class CollectionExAdapter<C : MutableCollection<T?>, T> private constructor(
    private val elementAdapter: JsonAdapter<T>
) : JsonAdapter<C>() {

    abstract fun newCollection(): C

    override fun fromJson(reader: JsonReader): C {
        val result = newCollection()
        reader.beginArray()
        while (reader.hasNext()) {
            result.add(elementAdapter.fromJson(reader))
        }
        reader.endArray()
        return result
    }

    override fun toJson(writer: JsonWriter, value: C?) {
        markNotNull(value) // Always wrapped in nullSafe()
        writer.beginArray()
        for (element in value) {
            elementAdapter.toJson(writer, element)
        }
        writer.endArray()
    }

    override fun toString() = "$elementAdapter.collection()"

    companion object FACTORY : Factory {
        override fun create(
            type: Type,
            annotations: Set<Annotation>,
            moshi: Moshi
        ): JsonAdapter<*>? {
            if (annotations.isNotEmpty()) return null
            return when (type.rawType) {
                ArrayList::class.java -> {
                    newArrayListAdapter<Any>(type, moshi).nullSafe()
                }
                HashSet::class.java -> {
                    newHashSetAdapter<Any>(type, moshi).nullSafe()
                }
                else -> null
            }
        }

        private fun <T> newArrayListAdapter(
            type: Type,
            moshi: Moshi
        ): JsonAdapter<ArrayList<T?>> {
            val elementType = Types.collectionElementType(type, Collection::class.java)
            val elementAdapter = moshi.adapter<T>(elementType)
            return object : CollectionExAdapter<ArrayList<T?>, T>(elementAdapter) {
                override fun newCollection(): ArrayList<T?> = ArrayList()
            }
        }

        private fun <T> newHashSetAdapter(type: Type, moshi: Moshi): JsonAdapter<HashSet<T?>> {
            val elementType = Types.collectionElementType(type, Collection::class.java)
            val elementAdapter = moshi.adapter<T>(elementType)
            return object : CollectionExAdapter<HashSet<T?>, T>(elementAdapter) {
                override fun newCollection(): HashSet<T?> = HashSet()
            }
        }
    }
}
