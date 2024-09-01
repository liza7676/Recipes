package com.example.recipes.domain

import com.example.recipes.data.API
import com.example.recipes.data.MainRepository
import com.example.recipes.data.PreferenceProvider
import com.example.recipes.utils.Converter
import com.example.recipes.data.entity.Recipes
import com.example.recipes.view.fragments.DataSearch
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject

class Interactor(private val repo: MainRepository, private val retrofitService: com.example.remote_module.TmdbApi,
                 private val preferences: PreferenceProvider
) {
    var progressBarState: BehaviorSubject<Boolean> = BehaviorSubject.create()
    var params = DataSearch(null, null, null, null, null)
    fun getRecipesFromApi(param: DataSearch) {
        //Показываем ProgressBar
        progressBarState.onNext(true)
        //Метод getDefaultCategoryFromPreferences() будет получать при каждом запросе нужный нам список рецептов

        if (!param.cuisine.equals("Any"))
            params.cuisine = param.cuisine
        if (!param.diet.equals("Any"))
            params.diet = param.diet
        if (!param.ingredients.equals("Any"))
            params.ingredients = param.ingredients
        if (!param.type.equals("Any"))
            params.type = param.type
        if (!param.time.equals("Any"))
            params.time = param.time
        retrofitService.getRecipes(params.cuisine,params.diet, params.ingredients, params.type, params.time,"2",  API.apiKey)
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
                    //repo.putToDb(it)
                        repo.putToList(it)
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

    fun getSummaryFromApi(id: String){
        //Показываем ProgressBar
        progressBarState.onNext(true)
        retrofitService.getSummary(id,  API.apiKey)
            .subscribeOn(Schedulers.io())
            .map {
                repo.setUrl(it.tmdbSourceUrl)
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
   // fun getParam() = preferences.gatParam()
    //fun setParam(param: DataSearch) = preferences.saveParam(param)
    fun setParam(param: DataSearch) = repo.setParam(param)
    fun getParam() = repo.getParams()

    fun getUrl() = repo.getUrl()
    fun getRecipesFromDB(): Observable<List<Recipes>> = repo.getAllFromDB()
    fun putToDb(list: List<Recipes>) = repo.putToDb(list)

    fun clearInCacheRecipes() = repo.clearInCacheRecipes()
    fun clearCache() = repo.clearCache()
    fun getRecipes(paramsSearch: DataSearch) = preferences.getRecipes()

    fun getSearchResultFromApi(search: String): Observable<List<Recipes>> = retrofitService.getRecipes(params.cuisine,params.diet, params.ingredients, params.type, params.time,"2",  API.apiKey)
        .map {
            Converter.convertApiListToDtoList(it.tmdbRecipes)
        }
    fun getFromList() : MutableList<Recipes>? = repo.getFromList()
}
