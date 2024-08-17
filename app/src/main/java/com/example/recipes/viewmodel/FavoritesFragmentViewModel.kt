package com.example.recipes.viewmodel

import androidx.lifecycle.ViewModel
import com.example.recipes.App
import com.example.recipes.data.entity.Recipes
import com.example.recipes.domain.Interactor
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

class FavoritesFragmentViewModel : ViewModel() {
    //Инициализируем интерактор
    @Inject
    lateinit var interactor: Interactor
    val recipesListData : Observable<List<Recipes>>
    val showProgressBar: BehaviorSubject<Boolean>

    init {
        App.instance.dagger.inject(this)
        showProgressBar = interactor.progressBarState
        recipesListData = interactor.getRecipesFromDB()
        getRecipes()
    }
    fun getRecipes() {
        interactor.getRecipesFromApi(1)
    }
}