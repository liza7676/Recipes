package com.example.recipes.di.modules

import android.content.Context
import androidx.room.Room
import com.example.recipes.AppDatabase
import com.example.recipes.data.DAO.RecipesDao
import com.example.recipes.data.MainRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabaseHelper(context: Context) = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "recipes_db"
    ).build().recipesDao()


    @Provides
    @Singleton
    fun provideRepository(recipesDao: RecipesDao) = MainRepository(recipesDao)
}