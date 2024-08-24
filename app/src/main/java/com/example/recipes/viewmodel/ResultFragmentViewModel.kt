package com.example.recipes.viewmodel

import androidx.lifecycle.ViewModel
import com.example.recipes.App
import com.example.recipes.domain.Interactor
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

class ResultFragmentViewModel  : ViewModel() {
    @Inject
    lateinit var interactor: Interactor
    val showProgressBar: BehaviorSubject<Boolean>
    init {
        App.instance.dagger.inject(this)
        showProgressBar = interactor.progressBarState
        getRecipes()
    }
    fun getRecipes(){
        interactor.getRecipesFromApi(1)
    }
}