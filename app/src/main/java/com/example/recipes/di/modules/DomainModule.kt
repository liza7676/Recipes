package com.example.recipes.di.modules

import android.content.Context
import com.example.recipes.data.MainRepository
import com.example.recipes.data.PreferenceProvider
import com.example.remote_module.TmdbApi
import com.example.recipes.domain.Interactor
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
//Передаем контекст для SharedPreferences через конструктор
class DomainModule(val context: Context) {
    //Нам нужно контекст как-то провайдить, поэтому создаем такой метод
    @Provides
    fun provideContext() = context

    @Singleton
    @Provides
    //Создаем экземпляр SharedPreferences
    fun providePreferences(context: Context) = PreferenceProvider(context)
    @Singleton
    @Provides
    fun provideInteractor(repository: MainRepository, tmdbApi: com.example.remote_module.TmdbApi, preferenceProvider: PreferenceProvider) =
        Interactor(repo = repository, retrofitService = tmdbApi, preferences = preferenceProvider)
}