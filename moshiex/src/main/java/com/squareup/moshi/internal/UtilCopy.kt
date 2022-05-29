@file:Suppress("NOTHING_TO_INLINE")
package com.squareup.moshi.internal

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract


@OptIn(ExperimentalContracts::class)
internal inline fun <T> markNotNull(value: T?) {
    contract {
        returns() implies (value != null)
    }
}
