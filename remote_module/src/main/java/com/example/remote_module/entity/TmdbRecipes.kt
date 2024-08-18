package com.example.remote_module.entity

import com.google.gson.annotations.SerializedName

data class TmdbRecipes (
    @SerializedName("id")
    val id: Int,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("title")
    val title: String

)

data class TmdbTrivia (
    @SerializedName("text")
    val text: String

)