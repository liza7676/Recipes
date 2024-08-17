package com.example.remote_module

import com.example.remote_module.entity.TmdbResultsDto
import io.reactivex.rxjava3.core.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApi {
    @GET("recipes/complexSearch?{number}")
    fun getRecipes(
        @Path("number") number: String,
        @Query("api_key") apiKey: String,
    ): Observable<TmdbResultsDto>

}