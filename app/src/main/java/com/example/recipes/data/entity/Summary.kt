package com.example.recipes.data.entity

import com.google.gson.annotations.SerializedName

data class Summary (
    var sourceUrl: String = "",
    var servings : Int = 0,
    var readyInMinutes: Int = 0,
    var vegetarian: Boolean = false,
    var dishTypes: List<String>,
    var tmdbIngredients: List<String>,
    var summary: String,
    var tmdbInstructions: List<String>
)


