package com.example.recipes

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.example.recipes.di.AppComponent
import com.example.recipes.di.DaggerAppComponent
import com.example.recipes.di.modules.DatabaseModule
import com.example.recipes.di.modules.DomainModule
import com.example.remote_module.DaggerRemoteComponent

class App : Application() {
    lateinit var dagger: AppComponent

    override fun onCreate() {
        super.onCreate()
        //Инициализируем экземпляр App, через который будем получать доступ к остальным переменным
        instance = this
//Создаем компонент
        val remoteProvider = DaggerRemoteComponent.create()
        dagger = DaggerAppComponent.builder()
            .remoteProvider(remoteProvider)
            .databaseModule(DatabaseModule())
            .domainModule(DomainModule(this))
            .build()

    }

    companion object {
        //Здесь статически хранится ссылка на экземпляр App
        lateinit var instance: App
            //Приватный сеттер, чтобы нельзя было в эту переменную присвоить что-либо другое
            private set
    }
}