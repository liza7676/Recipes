package com.example.recipes.utils

import com.example.recipes.data.entity.Recipes
import com.example.recipes.data.entity.Summary

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
    fun convertApiListToDtoListSummary(list: com.example.remote_module.entity.TmdbSummaryDto?): Summary {
        var ingredients = mutableListOf<String>()
        var instructions = mutableListOf<String>()
        val dishTypes = mutableListOf<String>()
        var summary = Summary( "Any", 0, 0, false, dishTypes, ingredients, "", instructions)
        if (list != null) {
            list?.dishTypes?.forEach {
                dishTypes.add(it )
            }
            list?.tmdbIngredients?.forEach{
                ingredients.add(it.original )
            }
            list?.tmdbInstructions?.forEach {
                it.steps.forEach{
                    instructions.add(it.step )
                }
            }

            summary.sourceUrl = list.tmdbSourceUrl
            summary.dishTypes = dishTypes
            summary.tmdbIngredients = ingredients
            summary.tmdbInstructions = instructions
            summary.servings = list.servings
            summary.readyInMinutes = list.readyInMinutes
            summary.vegetarian = list.vegetarian
            summary.summary = list.summary
        }
        return summary
    }
}