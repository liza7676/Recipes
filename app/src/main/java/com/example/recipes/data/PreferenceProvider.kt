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
        //Логика для первого запуска приложения, чтобы положить наши настройки,
        //Сюда потом можно добавить и другие настройки
        if(preference.getBoolean(KEY_FIRST_LAUNCH, false)) {
            preference.edit().putString(KEY_DEFAULT_CATEGORY, DEFAULT_CATEGORY).apply()
            preference.edit().putBoolean(KEY_FIRST_LAUNCH, false).apply()
            preference.edit().putLong(DOWNLOAD_TIME, 0).apply()
        }
    }

    //Category prefs
    //Сохраняем категорию
    fun saveDefaultCategory(category: String) {
        preference.edit().putString(KEY_DEFAULT_CATEGORY, category) .apply()
    }
    //Забираем категорию
    fun getDefaultCategory(): String {
        return preference.getString(KEY_DEFAULT_CATEGORY, DEFAULT_CATEGORY) ?: DEFAULT_CATEGORY
    }

    fun saveDounloadTime(data: Long) {
        preference.edit().putLong(DOWNLOAD_TIME, data) .apply()
    }
    fun getDounloadTime(): Long {
        return preference.getLong(DOWNLOAD_TIME, 0)
    }
    //Ключи для наших настроек, по ним мы их будем получать
    companion object {
        private const val KEY_FIRST_LAUNCH = "first_launch"
        private const val KEY_DEFAULT_CATEGORY = "default_category"
        private const val DEFAULT_CATEGORY = "popular"
        private const val DOWNLOAD_TIME = "dounload_time"
    }
}