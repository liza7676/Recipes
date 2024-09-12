package com.example.remote_module.entity

import com.google.gson.annotations.SerializedName

data class TmdbResultsDto (

    @SerializedName("results")
    val tmdbRecipes: List<com.example.remote_module.entity.TmdbRecipes>,
    @SerializedName("offset")
    val offset: Int,
    @SerializedName("number")
    val number: Int,
    @SerializedName("totalResults")
    val totalResults: Int
)
data class TmdbTriviaDto (
    @SerializedName("text")
    val tmdbTrivia: String
)

data class TmdbSummaryDto (
    @SerializedName("sourceUrl")
    val tmdbSourceUrl: String,
    @SerializedName("servings") //порции
    val servings: Int,
    @SerializedName("readyInMinutes")
    val readyInMinutes: Int,
    @SerializedName("vegetarian")
    val vegetarian: Boolean,
    @SerializedName("dishTypes")
    val dishTypes: List<String>,
    @SerializedName("extendedIngredients")
    val tmdbIngredients: List<com.example.remote_module.entity.TmdbIngredients>,
    @SerializedName("summary")
    val summary: String,
    @SerializedName("analyzedInstructions")
    val tmdbInstructions: List<com.example.remote_module.entity.TmdbInstructions>
)