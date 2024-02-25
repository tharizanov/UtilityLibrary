package uk.co.accsoft.util.extensions

inline fun <T : Collection<*>> T.ifNotEmpty(action: (T) -> Unit): T {
    if (isNotEmpty()) action(this)
    return this
}

inline fun <T> Iterable<T>.forEachDivided(
    betweenLoops: (index: Int, T) -> Unit,
    action: (index: Int, T) -> Unit
) = forEachIndexed { index, item ->
    if (index > 0 && index < count()) {
        betweenLoops(index, item)
    }
    action(index, item)
}

inline fun <T> Iterable<T>.forEachMatching(
    criteria: (T) -> Boolean,
    action: (T) -> Unit
) = forEach {
    if (criteria(it)) action(it)
}

inline fun <T> Iterable<T>.forEachMatchingIndexed(
    criteria: (index: Int, T) -> Boolean,
    action: (index: Int, T) -> Unit
) = forEachIndexed { index, item ->
    if (criteria(index, item)) action(index, item)
}