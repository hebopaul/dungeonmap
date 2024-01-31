package com.example.dungeonmap.utilities


import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.example.dungeonmap.data.StockImage

//the following function recovers the ids of all the drawable resources files and returns them in a list
//along with their names as Pairs
@Composable
fun getDrawableResourcesIds(prefix: String): List<StockImage> {
    val field = Class.forName("com.example.dungeonmap.R\$drawable").declaredFields
    val list: MutableList<StockImage> = mutableListOf()
    field.forEach {
        if(prefix in it.name) list.add(
            StockImage (
                id = it.getInt(it),
                name = beautifyResName(it.name),
                image = painterResource( it.getInt(it) )
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