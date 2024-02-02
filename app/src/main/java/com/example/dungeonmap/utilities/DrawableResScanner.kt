package com.example.dungeonmap.utilities


import com.example.dungeonmap.data.StockImage

//the following function recovers the ids of all the drawable resources files and returns them in a list
//along with their names as Pairs

fun getDrawableResourcesIds(prefix: String): List<StockImage> {
    val field = Class.forName("com.example.dungeonmap.R\$drawable").declaredFields
    val list: MutableList<StockImage> = mutableListOf()
    field.forEach {

        if(prefix in it.name) list.add(
            StockImage (
                id = it.getInt(it),
                name = beautifyResName(it.name)
            )
        )
    }
    return list
}

fun beautifyResName(name: String): String =
    name
        .split("_")
        .filter { it.isNotEmpty() && it !in listOf("map", "token") }
        .joinToString(" ") { it.replaceFirstChar { it.uppercase() } }