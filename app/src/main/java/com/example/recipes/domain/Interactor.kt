package com.example.recipes.domain

import com.example.recipes.data.API
import com.example.recipes.data.MainRepository
import com.example.recipes.data.PreferenceProvider
import com.example.remote_module.TmdbApi
import com.example.recipes.utils.Converter
import com.example.recipes.data.entity.Recipes
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class Interactor(private val repo: MainRepository, private val retrofitService: com.example.remote_module.TmdbApi,
                 private val preferences: PreferenceProvider
) {
    var progressBarState: BehaviorSubject<Boolean> = BehaviorSubject.create()
    fun getRecipesFromApi(page: Int) {
        //Показываем ProgressBar
        progressBarState.onNext(true)
        //Метод getDefaultCategoryFromPreferences() будет получать при каждом запросе нужный нам список рецептов
        retrofitService.getRecipes("1", API.apiKey)
            .subscribeOn(Schedulers.io())
            .map {
                Converter.convertApiListToDtoList(it.tmdbRecipes)
            }
            .subscribeBy(
                onError = {
                    progressBarState.onNext(false)
                },
                onNext = {
                    progressBarState.onNext(false)
                    repo.putToDb(it)
                }
            )
    }

    fun getTriviaFromApi(){
        //Показываем ProgressBar
        progressBarState.onNext(true)
        retrofitService.getTrivia( API.apiKey)
            .subscribeOn(Schedulers.io())
            .map {
                preferences.saveTrivia(it.tmdbTrivia)
            }
            .subscribeBy(
                onError = {
                    progressBarState.onNext(false)
                },
                onNext = {
                    progressBarState.onNext(false)
                }
            )
    }

    fun getTrivia() = preferences.getTrivia()

    fun getRecipesFromDB(): Observable<List<Recipes>> = repo.getAllFromDB()

    fun clearCache() = repo.clearCache()
    fun getRecipes() = preferences.getRecipes()

}
