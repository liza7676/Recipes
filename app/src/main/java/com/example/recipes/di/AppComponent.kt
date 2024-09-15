package com.example.recipes.di

import com.example.recipes.di.modules.DatabaseModule
import com.example.recipes.di.modules.DomainModule
import com.example.recipes.view.fragments.DetailsFragment
import com.example.recipes.view.fragments.FavoritesFragment
import com.example.recipes.view.fragments.ResultFragment
import com.example.recipes.view.fragments.SearchFragment
import com.example.recipes.viewmodel.DetailsFragmentViewModel
import com.example.recipes.viewmodel.FavoritesFragmentViewModel
import com.example.recipes.viewmodel.ResultFragmentViewModel
import com.example.recipes.viewmodel.SearchFragmentViewModel
import com.example.recipes.viewmodel.ViewedFragmentViewModel
import dagger.Component
import javax.inject.Singleton
import com.example.remote_module.RemoteProvider

@Singleton
@Component(
    dependencies = [RemoteProvider::class],
     modules = [
        DatabaseModule::class,
        DomainModule::class
    ]
)

interface AppComponent {
    fun inject(favoritesFragment: FavoritesFragmentViewModel)
    fun inject(resultFragment: ResultFragmentViewModel)
    fun inject(searchFragment: SearchFragmentViewModel)
    fun inject(viewedFragment: ViewedFragmentViewModel)
    fun inject(detailsFragment: DetailsFragmentViewModel)
}