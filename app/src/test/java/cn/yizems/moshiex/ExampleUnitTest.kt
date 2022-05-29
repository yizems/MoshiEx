package cn.yizems.moshiex

import cn.yizems.moshi.ex.moshiInstances
import cn.yizems.moshi.ex.setToDefault
import cn.yizems.moshi.ex.toJsonString
import cn.yizems.moshiex.moshi.TestVal
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testVal() {
        moshiInstances.newBuilder()
            .add(KotlinJsonAdapterFactory())
            .build()
            .setToDefault()
        println(TestVal().toJsonString())
    }
}
