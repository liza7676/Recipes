package com.example.remote_module.entity

import com.google.gson.annotations.SerializedName

data class TmdbRecipes (
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("title")
    val title: String

)

data class TmdbTrivia (
    @SerializedName("text")
    val text: String

)

data class TmdbSummary (
    @SerializedName("id")
    val id: Int,
    @SerializedName("sourceUrl")
    val sourceUrl: String,
    @SerializedName("title")
    val title: String

)