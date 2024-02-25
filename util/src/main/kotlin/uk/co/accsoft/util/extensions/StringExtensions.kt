package uk.co.accsoft.util.extensions

inline fun <T : CharSequence> T.ifNotEmpty(action: (T) -> Unit): T {
    if (isNotEmpty()) action(this)
    return this
}

val Any.TAG: String
    get() = this.javaClass.simpleName.takeLast(23)

fun Any?.toReadableString(iterableSeparator: CharSequence = ", "): String =
    when (this) {
        is Iterable<*> -> iterableToString(this, iterableSeparator)
        is Map<*, *> -> mapToString(this, iterableSeparator)
        is Throwable -> throwableToString(this)
        else -> toString().ifEmpty { "_" }
    }


//////////////////// Private functions ///////////////////////////

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
        append(throwable.javaClass.name)
        append(": ")
        throwable.message?.let { append(it) }
        for (ste in throwable.stackTrace) {
            append("\nat ")
            append(ste.className)
            append(".")
            append(ste.methodName)
            append("()")
        }
    }.toString()