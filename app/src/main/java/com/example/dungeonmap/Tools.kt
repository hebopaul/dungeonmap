package com.example.dungeonmap

//the following function recovers the ids of all the drawable resources files and returns them in a list
//along with their names as Pairs
fun getDrawableResourcesIds(prefix: String): List<Pair<Int, String>> {
    val field = Class.forName("com.example.dungeonmap.R\$drawable").declaredFields
    val list: MutableList<Pair<Int, String>> = mutableListOf( Pair(0, ""))
    list.clear()
    field.forEach {
        if(prefix in it.name) list.add(Pair ( it.getInt(it), it.name))
    }
    return list
}
