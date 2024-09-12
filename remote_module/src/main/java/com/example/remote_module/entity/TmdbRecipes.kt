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

data class TmdbInstructions(
    @SerializedName("steps")
    val steps: List<com.example.remote_module.entity.TmdbSteps>
)
data class  TmdbIngredients(
    @SerializedName("original")
    val original: String
)

data class  TmdbSteps(
    @SerializedName("step")
    val step: String
)