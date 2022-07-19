package com.codetest.myweather.room

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class CityLocationRepository(private val cityLocationDao: CityLocationDao) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allCityLocations: Flow<List<CityLocation>> = cityLocationDao.getAlphabetizedCityLocations()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(city_location: CityLocation) {
        cityLocationDao.insert(city_location)
    }

    @WorkerThread
    suspend fun delete(city_location: CityLocation) {
        cityLocationDao.delete(city_location)
    }
}
