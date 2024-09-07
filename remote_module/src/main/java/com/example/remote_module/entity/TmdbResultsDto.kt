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
    val tmdbSourceUrl: String
)