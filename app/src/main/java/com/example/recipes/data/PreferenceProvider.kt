package com.example.recipes.data

import android.content.Context
import android.content.SharedPreferences
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

    fun getTrivia(): String {
        return preference.getString(TRIVIA, DEFAULT_TRIVIA) ?: DEFAULT_TRIVIA
    }

    //Ключи для наших настроек, по ним мы их будем получать
    companion object {
        private const val DEFAULT_TRIVIA = "no data"
        private const val TRIVIA = "trivia"
    }
}