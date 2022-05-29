package cn.yizems.moshi.ex.util

import java.math.BigDecimal

/**
 * 是否有小数位
 */
internal fun Double.hasFractional(): Boolean {
  val b = BigDecimal(this)
  return b.scale() > 0
}
