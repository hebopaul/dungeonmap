package com.example.dungeonmap.utilities


import com.example.dungeonmap.data.StockImage



// NK: These methods have to much in common.
// Could be one? Could the first feed the other?
fun getStockImageList(prefix: String): List<StockImage> {
    return Class
        .forName("com.example.dungeonmap.R\$drawable")
        .declaredFields
        .filter { prefix in it.name }
        .map { StockImage(
            id = it.getInt(it),
            name = beautifyResName(it.name)
        ) }
}

fun getDrawableResourcesIds(prefix: String): List<Int> {
    return Class
        .forName("com.example.dungeonmap.R\$drawable")
        .declaredFields
        .filter { prefix in it.name }
        .map { it.getInt(it) }
}

fun beautifyResName(name: String): String =
    name.split("_")
        .filter { it.isNotEmpty() }
        .filter { it !in listOf("map", "token") }
        .joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }