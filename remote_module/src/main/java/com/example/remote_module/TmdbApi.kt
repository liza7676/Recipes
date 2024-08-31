package com.example.remote_module

import com.example.remote_module.entity.ApiConstants.TRIVIA
import com.example.remote_module.entity.TmdbResultsDto
import com.example.remote_module.entity.TmdbSummaryDto
import com.example.remote_module.entity.TmdbTriviaDto
import io.reactivex.rxjava3.core.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface TmdbApi {
    @GET("recipes/complexSearch?")
    fun getRecipes(
        @Query("cuisine") cuisine: String?,
        @Query("diet") diet: String?,
        @Query("includeIngredients") includeIngredients: String?,
        @Query("type") type: String?,
        @Query("maxReadyTime") maxReadyTime: String?,
        @Query("number") number: String,
        @Query("apiKey") apiKey: String,
    ): Observable<TmdbResultsDto>

    @GET(TRIVIA)
    fun getTrivia(
        @Query("apiKey") apiKey: String,
    ): Observable<TmdbTriviaDto>

    @GET("recipes/{id}/information?includeNutrition=false")
    fun getSummary(
        @Path("id") id: String,
        @Query("apiKey") apiKey: String,
    ): Observable<TmdbSummaryDto>
}