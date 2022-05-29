@file:Suppress("ObjectPropertyName")

package cn.yizems.moshi.ex

import cn.yizems.moshi.ex.adapter.Base64QualifierAdapter
import cn.yizems.moshi.ex.adapter.EnumJsonTypeAdapter
import cn.yizems.moshi.ex.adapter.JsonCodecAdapter
import cn.yizems.moshi.ex.adapter.RewriteStandardJsonAdapters
import com.squareup.moshi.CollectionExAdapter
import com.squareup.moshi.HashMapJsonAdapter
import com.squareup.moshi.Moshi

/**
 * 默认模块和属性, 用于内部或其他衍生库
 */

private var _moshInstances: Moshi = Moshi
  .Builder()
  .add(RewriteStandardJsonAdapters())
  .add(JsonCodecAdapter.FACTORY)
  .add(EnumJsonTypeAdapter.FACTORY)
  .add(Base64QualifierAdapter.FACTORY)
  .add(CollectionExAdapter.FACTORY)
  .add(HashMapJsonAdapter.FACTORY)
  .build()

public val moshiInstances: Moshi
  get() = _moshInstances

public fun Moshi.setToDefault() {
  _moshInstances = this
}


