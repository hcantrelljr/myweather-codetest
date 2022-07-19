package com.codetest.myweather

import android.app.Application
import com.codetest.myweather.room.CityLocationRepository
import com.codetest.myweather.room.CityLocationRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MyWeatherApplication : Application() {
    // No need to cancel this scope as it'll be torn down with the process
    private val applicationScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    private val database by lazy { CityLocationRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { CityLocationRepository(database.cityLocationDao()) }
}
