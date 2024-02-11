package uk.co.accsoft.util.extensions

import java.io.Closeable
import java.io.IOException
import kotlin.reflect.full.declaredMemberProperties

fun Closeable.closeQuietly() = try { close() } catch (_: IOException) {}

inline fun <T> Iterable<T>.forEachMatching(criteria: (T) -> Boolean, action: (T) -> Unit) =
    forEach { if (criteria(it)) action(it) }

inline fun <T> Iterable<T>.forEachMatchingIndexed(
    criteria: (index: Int, T) -> Boolean,
    action: (index: Int, T) -> Unit
) = forEachIndexed { index, item ->
    if (criteria(index, item)) action(index, item)
}

inline fun <T : Collection<*>> T.ifNotEmpty(action: (T) -> Unit): T {
    if (isNotEmpty()) action(this)
    return this
}

inline fun <T : Any> T?.ifNull(action: () -> Unit): T? {
    if (this == null) action()
    return this
}

fun Any.propertiesMap(): Map<String, Any?> =
    javaClass.kotlin.declaredMemberProperties.associate { it.name to it.get(this) }