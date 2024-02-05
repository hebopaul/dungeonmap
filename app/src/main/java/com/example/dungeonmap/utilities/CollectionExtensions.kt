package com.example.dungeonmap.utilities


fun <T, C: Collection<T>> C.transformItem(predicate: (T) -> Boolean, transformation: (T) -> T): List<T> {
    val index = indexOfFirst(predicate)
    return when (index < 0) {
        true -> this.toList()
        else -> this
            .toMutableList()
            .also { it[index] = transformation(it[index]) }
            .toList()
    }
}