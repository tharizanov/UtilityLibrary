package uk.co.accsoft.util.extensions

import java.io.Closeable
import java.io.IOException
import kotlin.reflect.full.declaredMemberProperties

fun Any.classPropertiesMap(): Map<String, Any?> =
    javaClass.kotlin.declaredMemberProperties.associate { it.name to it.get(this) }

fun Closeable.closeQuietly() =
    try { close() } catch (_: IOException) {}

fun Comparable<Number>.isBetween(start: Int, end: Int): Boolean =
    this > start && this < end

inline fun <T : Any> T?.ifNull(action: () -> Unit): T? {
    if (this == null) action()
    return this
}