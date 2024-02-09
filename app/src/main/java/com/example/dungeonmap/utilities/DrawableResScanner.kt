package com.example.dungeonmap.utilities


import com.example.dungeonmap.data.StockImage



fun getStockImageList(prefix: String): List<StockImage> {
    val field = Class.forName("com.example.dungeonmap.R\$drawable").declaredFields
    val list: MutableList<StockImage> = mutableListOf()
    field.forEach {

        if(prefix in it.name) list.add(
            StockImage (
                id = it.getInt(it),
                name = beautifyResName(it.name),
            )
        )
    }
    return list
}

fun getDrawableResourcesIds(prefix: String): List<Int> {
    val field = Class.forName("com.example.dungeonmap.R\$drawable").declaredFields
    val list: MutableList<Int> = mutableListOf()
     field.forEach { if (prefix in it.name) list.add(it.getInt(it))}
    return list
}

fun getNameFromResId(resId: Int): String {
    val field = Class.forName("com.example.dungeonmap.R\$drawable").declaredFields
    return beautifyResName( field.find { it.getInt(it) == resId }?.name.toString() )
}


fun beautifyResName(name: String): String =
    name
        .split("_")
        .filter { it.isNotEmpty() && it !in listOf("map", "token") }
        .joinToString(" ") { it.replaceFirstChar { it.uppercase() } }
