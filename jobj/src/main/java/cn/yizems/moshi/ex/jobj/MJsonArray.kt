/**
 * Copyright (c) 2021 yizems
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.yizems.moshi.ex.jobj

import com.squareup.moshi.kotlin.jobj.TypeUtils.castToBigDecimal
import com.squareup.moshi.kotlin.jobj.TypeUtils.castToBigInteger
import com.squareup.moshi.kotlin.jobj.TypeUtils.castToBoolean
import com.squareup.moshi.kotlin.jobj.TypeUtils.castToDouble
import com.squareup.moshi.kotlin.jobj.TypeUtils.castToFloat
import com.squareup.moshi.kotlin.jobj.TypeUtils.castToInt
import com.squareup.moshi.kotlin.jobj.TypeUtils.castToLong
import com.squareup.moshi.kotlin.jobj.TypeUtils.castToString
import com.squareup.moshi.kotlin.jobj.TypeUtils.wrapValue
import java.io.Serializable
import java.math.BigDecimal
import java.math.BigInteger

typealias JsonArray = MJsonArray

class MJsonArray : Serializable {

    private var innerList: MutableList<Any?>

    constructor() {
        innerList = ArrayList(10)
    }

    constructor(list: List<Any?>?) {
        this.innerList = list?.toMutableList() ?: ArrayList(10)

        if (list == null) {
            this.innerList = ArrayList(10)
            return
        }

        this.innerList = ArrayList(list.size)

        list.forEach {
            innerList.add(wrapValue(it))
        }
    }

    constructor(initialCapacity: Int) {
        innerList = ArrayList(initialCapacity)
    }

    fun size(): Int {
        return innerList.size
    }

    fun isEmpty(): Boolean = innerList.isEmpty()

    operator fun contains(o: Any): Boolean {
        return innerList.contains(o)
    }

    operator fun iterator(): Iterator<Any?> {
        return innerList.iterator()
    }

    fun add(e: Any?): Boolean {
        return innerList.add(wrapValue(e))
    }

    fun remove(o: Any?): Boolean {
        return innerList.remove(o)
    }

    fun containsAll(c: Collection<*>): Boolean {
        return innerList.containsAll(c)
    }

    fun addAll(c: Collection<Any?>): Boolean {
        c.forEach {
            innerList.add(wrapValue(it))
        }
        return true
    }

    fun removeAll(c: Collection<*>): Boolean {
        return innerList.removeAll(c)
    }

    fun retainAll(c: Collection<*>): Boolean {
        return innerList.retainAll(c)
    }

    fun clear() {
        innerList.clear()
    }

    operator fun set(index: Int, element: Any?): Any? {
        return innerList.set(index, wrapValue(element))
    }

    fun add(index: Int, element: Any?) {
        innerList.add(index, wrapValue(element))
    }

    fun remove(index: Int): Any? {
        return innerList.removeAt(index)
    }

    fun indexOf(o: Any?): Int {
        return innerList.indexOf(o)
    }

    fun lastIndexOf(o: Any?): Int {
        return innerList.lastIndexOf(o)
    }

    fun listIterator(): ListIterator<Any?> {
        return innerList.listIterator()
    }

    fun listIterator(index: Int): ListIterator<Any?> {
        return innerList.listIterator(index)
    }

    fun subList(fromIndex: Int, toIndex: Int): List<Any?> {
        return innerList.subList(fromIndex, toIndex)
    }

    operator fun get(index: Int): Any? {
        return innerList[index]
    }


    fun getBoolean(index: Int): Boolean? {
        val value = get(index) ?: return null
        return castToBoolean(value)
    }

    fun getBooleanValue(index: Int): Boolean {
        return getBoolean(index) ?: return false
    }

    fun getInteger(index: Int): Int? {
        val value = get(index)
        return castToInt(value)
    }

    fun getIntValue(index: Int): Int {
        return getInteger(index) ?: 0
    }

    fun getLong(index: Int): Long? {
        val value = get(index)
        return castToLong(value)
    }

    fun getLongValue(index: Int): Long {
        return getLong(index) ?: 0L
    }

    fun getFloat(index: Int): Float? {
        val value = get(index)
        return castToFloat(value)
    }

    fun getFloatValue(index: Int): Float {
        return getFloat(index) ?: 0F
    }

    fun getDouble(index: Int): Double? {
        val value = get(index)
        return castToDouble(value)
    }

    fun getDoubleValue(index: Int): Double {
        return getDouble(index) ?: 0.0
    }

    fun getBigDecimal(index: Int): BigDecimal? {
        val value = get(index)
        return castToBigDecimal(value)
    }

    fun getBigInteger(index: Int): BigInteger? {
        val value = get(index)
        return castToBigInteger(value)
    }

    fun getString(index: Int): String? {
        val value = get(index)
        return castToString(value)
    }

    fun clone(): Any {
        return MJsonArray(innerList)
    }

    override fun equals(other: Any?): Boolean {
        return innerList == other
    }

    override fun hashCode(): Int {
        return innerList.hashCode()
    }

    fun getMJsonObject(index: Int): MJsonObject? {
        return innerList[index] as? MJsonObject
    }

    fun getMJsonArray(index: Int): MJsonArray? {
        return innerList[index] as? MJsonArray
    }

    fun getInnerList(): List<Any?> = innerList
}
