package com.example.recipes

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.recipes.data.DAO.RecipesDao
import com.example.recipes.data.entity.Recipes

@Database(entities = [Recipes::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recipesDao(): RecipesDao
}