package com.example.recipes.data

import com.example.recipes.utils.AutoDisposable
import com.example.recipes.utils.addTo
import com.example.recipes.data.DAO.RecipesDao
import com.example.recipes.data.entity.Recipes
import com.example.recipes.view.fragments.DataSearch
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers


class MainRepository(private val recipesDao: RecipesDao) {
    private val autoDisposable = AutoDisposable()
    var recipesList: MutableList<Recipes>? = null
    private var params = DataSearch("Any", "Any", "Any", "Any", "Any" )
    var recipesUrl = "Any"
    fun putToDb(recipes: List<Recipes>) {
        //Запросы в БД должны быть в отдельном потоке
        recipesDao.insertAll(recipes)
    }

    fun getFromDBFavorites(): Observable<List<Recipes>> = recipesDao.getCachedRecipesFavorites()
    fun getFromDBViewed(): Observable<List<Recipes>> = recipesDao.getCachedRecipesViewed()

    fun putToList(recipes: List<Recipes>){
       // recipesList?.clear()recipes
        if(recipesList == null ){
            recipesList = recipes.toMutableList()
        } else {
            recipes.forEach {
                recipesList?.add(it)
            }
        }
    }
    fun getFromList() : MutableList<Recipes>?{
        return recipesList
    }
    fun setParam(param: DataSearch) {
        params.cuisine = param.cuisine
        params.diet = param.diet
        params.ingredients = param.ingredients
        params.type = param.type
        params.time = param.time
    }
    fun getParams(): DataSearch{
        return params
    }
    fun delFromFavorites(recipes: Recipes){
        val rec = recipesDao.getCachedRecipes(recipes.id)

        if(rec != null)
            recipesDao.deleteRecipes(rec)
    }

    fun searchFromFavorites(id: Int): Recipes?{
        return recipesDao.getCachedRecipes(id)
    }
    fun getUrl(): String{
        return recipesUrl
    }
    fun setUrl(url: String){
        recipesUrl = url
    }
}