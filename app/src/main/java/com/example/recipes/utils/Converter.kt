package com.example.recipes.utils

import com.example.recipes.data.entity.Recipes

object Converter {

    fun convertApiListToDtoList(list: List<com.example.remote_module.entity.TmdbRecipes>?): List<Recipes> {
        val result = mutableListOf<Recipes>()
        list?.forEach {
            result.add(Recipes(
                title = it.title,
                poster = it.image,
                id = it.id,
                isInFavorites = false,
                isViewed = false
            ))
        }
        return result
    }
    fun convertApiListToDtoListTrivia(list: com.example.remote_module.entity.TmdbTrivia?): String {
//        val result = mutableListOf<String>()
//        list?.forEach {
//            result.add(it.text )
//        }
        return list?.text ?: "-"
    }
}