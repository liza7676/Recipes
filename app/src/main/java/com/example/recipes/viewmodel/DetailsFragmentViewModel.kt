package com.example.recipes.viewmodel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import com.example.recipes.App
import com.example.recipes.domain.Interactor
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.io.IOException
import java.net.URL
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class DetailsFragmentViewModel  : ViewModel() {
    @Inject
    lateinit var interactor: Interactor
    val showProgressBar: BehaviorSubject<Boolean>

    init {
        App.instance.dagger.inject(this)
        showProgressBar = interactor.progressBarState
    }
    fun getSummary(id: String){
        interactor.getSummaryFromApi(id)
    }
}