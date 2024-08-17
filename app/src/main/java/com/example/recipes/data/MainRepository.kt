package com.example.recipes.data

import com.example.recipes.utils.AutoDisposable
import com.example.recipes.utils.addTo
import com.example.recipes.data.DAO.RecipesDao
import com.example.recipes.data.entity.Recipes
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers


class MainRepository(private val recipesDao: RecipesDao) {
    private val autoDisposable = AutoDisposable()
    fun putToDb(recipes: List<Recipes>) {
        //Запросы в БД должны быть в отдельном потоке
        recipesDao.insertAll(recipes)
    }

    fun getAllFromDB(): Observable<List<Recipes>> = recipesDao.getCachedRecipes()

     fun clearCache(){

        val cachedRecipes = recipesDao.getCachedRecipes()
        cachedRecipes.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { list ->
                    recipesDao.deleteDB(list)
                }
                .addTo(autoDisposable)
    }

}