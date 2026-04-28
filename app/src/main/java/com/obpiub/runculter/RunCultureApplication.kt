package com.obpiub.runculter

import android.app.Application
import androidx.room.Room
import com.obpiub.runculter.data.database.RunCultureDatabase
import com.obpiub.runculter.data.repository.AppRepository
import com.yandex.mapkit.MapKitFactory
import com.obpiub.runculter.data.database.DatabaseInitializer

class RunCultureApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Установка API ключа для MapKit
        // Согласно документации: https://yandex.ru/maps-api/docs/mapkit/android/generated/getting_started.html
        MapKitFactory.setApiKey("6b5910c5-5465-4ca0-aa79-5757d5bf2309")
        // MapKitFactory.initialize() вызывается в Activity/Fragment, а не здесь
        
        // Инициализация тестовых данных при первом запуске
        val initializer = DatabaseInitializer(repository)
        initializer.initializeTestData()
    }

    val database by lazy {
        Room.databaseBuilder(
            applicationContext,
            RunCultureDatabase::class.java,
            "runculture_database"
        ).build()
    }

    val repository by lazy {
        AppRepository(
            database.userDao(),
            database.landmarkDao(),
            database.characterDao(),
            database.historicalFactDao()
        )
    }
}
