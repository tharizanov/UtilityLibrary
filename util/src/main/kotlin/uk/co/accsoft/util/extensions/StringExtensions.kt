package uk.co.accsoft.util.extensions

inline fun <T : CharSequence> T.ifNotEmpty(action: (T) -> Unit): T {
    if (isNotEmpty()) action(this)
    return this
}

fun Any?.toReadableString(iterableSeparator: CharSequence = ", "): String =
    when (this) {
        is Iterable<*> -> iterableToString(this, iterableSeparator)
        is Map<*, *> -> mapToString(this, iterableSeparator)
        is Throwable -> throwableToString(this)
        else -> toString().ifEmpty { "_" }
    }

private fun iterableToString(iterable: Iterable<*>, separator: CharSequence = ", "): String =
    iterable.joinToString(
        prefix = "[",
        separator = separator,
        postfix = "]"
    ) {
        it.toReadableString(separator)
    }

private fun mapToString(map: Map<*,*>, separator: CharSequence = ", "): String =
    map.entries.joinToString(
        prefix = "[",
        separator = separator,
        postfix = "]"
    ) {
        "${it.key}=${it.value.toReadableString(separator)}"
    }

private fun throwableToString(throwable: Throwable): String =
    StringBuilder("\n").apply {
        append(throwable::class.simpleName ?: "unknown Throwable")
        append(": ")
        append(throwable.message)
        for (ste in throwable.stackTrace) {
            append("\nat ")
            append(ste.className)
            append(".")
            append(ste.methodName)
            append("()")
        }
    }.toString()