package com.example.remote_module.entity

import com.google.gson.annotations.SerializedName

data class TmdbResultsDto (
    @SerializedName("count")
    val count: Int,
    @SerializedName("results")
    val tmdbRecipes: List<com.example.remote_module.entity.TmdbRecipes>,
)
data class TmdbTriviaDto (
    @SerializedName("text")
    val tmdbTrivia: String
    //val tmdbTrivia: com.example.remote_module.entity.TmdbTrivia
    //val tmdbTrivia: com.example.remote_module.entity.TmdbTrivia,
)