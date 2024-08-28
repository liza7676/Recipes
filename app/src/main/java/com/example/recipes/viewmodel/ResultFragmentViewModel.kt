package com.example.recipes.viewmodel

import androidx.lifecycle.ViewModel
import com.example.recipes.App
import com.example.recipes.MainActivity
import com.example.recipes.data.entity.Recipes
import com.example.recipes.domain.Interactor
import com.example.recipes.view.fragments.DataSearch
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

class ResultFragmentViewModel  : ViewModel() {
    @Inject
    lateinit var interactor: Interactor
    val recipesListData : Observable<List<Recipes>>
    val showProgressBar: BehaviorSubject<Boolean>
    val paramsSearch = DataSearch("Any", "Any", "Any", "Any", "Any")

    init {
        App.instance.dagger.inject(this)
        showProgressBar = interactor.progressBarState
        recipesListData = interactor.getRecipesFromDB()
        //getRecipes()
    }
    fun getRecipes(param: DataSearch){
        interactor.getRecipesFromApi(param)
    }

    fun getSearchResult(search: String) = interactor.getSearchResultFromApi(search)
}