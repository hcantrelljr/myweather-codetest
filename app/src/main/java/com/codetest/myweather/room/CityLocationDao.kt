package com.codetest.myweather.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CityLocationDao {

    @Query("SELECT * FROM city_location_table ORDER BY city_location ASC")
    fun getAlphabetizedCityLocations(): Flow<List<CityLocation>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(cityLocation: CityLocation)

    @Delete
    suspend fun delete(cityLocation: CityLocation)

    @Query("DELETE FROM city_location_table")
    suspend fun deleteAll()
}
