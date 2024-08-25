package com.example.recipes.data

import android.content.Context
import android.content.SharedPreferences
import com.example.recipes.view.fragments.DataSearch
import java.util.Calendar

class PreferenceProvider(context: Context) {
    //Нам нужен контекст приложения
    private val appContext = context.applicationContext
    //Создаем экземпляр SharedPreferences
    private val preference: SharedPreferences = appContext.getSharedPreferences("settings", Context.MODE_PRIVATE)

    init {

    }


    fun saveTrivia(trivia: String) {
        preference.edit().putString(TRIVIA, trivia) .apply()
    }
    fun saveParam(param: DataSearch) {
        preference.edit().putString(CUISINE, param.cuisine) .apply()
        preference.edit().putString(DIET, param.diet) .apply()
        preference.edit().putString(INGREDIENTS, param.ingredients) .apply()
        preference.edit().putString(TYPE, param.type) .apply()
        preference.edit().putString(TIME, param.time) .apply()
    }
    fun gatParam(): DataSearch
    {
       return DataSearch(preference.getString(CUISINE, "Any") ?: "Any",
           preference.getString(DIET, "Any") ?: "Any",
           preference.getString(INGREDIENTS, "Any") ?: "Any",
           preference.getString(TYPE, "Any") ?: "Any",
           preference.getString(TIME, "Any") ?: "Any",
           )
    }
    fun getTrivia(): String {
        return preference.getString(TRIVIA, DEFAULT_TRIVIA) ?: DEFAULT_TRIVIA
    }

    fun getRecipes(): String {
        return preference.getString(TRIVIA, DEFAULT_TRIVIA) ?: DEFAULT_TRIVIA
    }

    //Ключи для наших настроек, по ним мы их будем получать
    companion object {
        private const val DEFAULT_TRIVIA = "no data"
        private const val TRIVIA = "The fig is also a fertility symbol and the Arab association with male genitals is so strong that the original word 'fig' is considered improper."
        private const val CUISINE = "Any"
        private const val DIET = "Any"
        private const val INGREDIENTS = "Any"
        private const val TYPE = "Any"
        private const val TIME = "Any"
    }
}