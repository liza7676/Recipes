package com.example.recipes.data.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.recipes.data.entity.Recipes
import io.reactivex.rxjava3.core.Observable

@Dao
interface RecipesDao {
    //Запрос на всю таблицу
    @Query("SELECT * FROM cached_recipes WHERE id == :idRecipes")
    fun getCachedRecipes(idRecipes: Int): Recipes

    @Query("SELECT * FROM cached_recipes WHERE isInFavorites = true")
    fun getCachedRecipesFavorites(): Observable<List<Recipes>>

    @Query("SELECT * FROM cached_recipes WHERE isViewed = true")
    fun getCachedRecipesViewed(): Observable<List<Recipes>>

    //Кладём списком в БД, в случае конфликта перезаписываем
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<Recipes>)

    @Delete
    fun deleteDB(list: List<Recipes>)

    @Delete
    fun deleteRecipes(recipes:Recipes)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipes(recipes: Recipes)

}