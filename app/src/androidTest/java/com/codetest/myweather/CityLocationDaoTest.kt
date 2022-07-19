package com.codetest.myweather

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.codetest.myweather.room.CityLocation
import com.codetest.myweather.room.CityLocationDao
import com.codetest.myweather.room.CityLocationRoomDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class CityLocationDaoTest {
    private lateinit var cityLocationDao: CityLocationDao
    private lateinit var db: CityLocationRoomDatabase

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, CityLocationRoomDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        cityLocationDao = db.cityLocationDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetCityLocation() = runBlocking {
        val cityLocation = CityLocation("cityLocation")
        cityLocationDao.insert(cityLocation)
        val allCityLocations = cityLocationDao.getAlphabetizedCityLocations().first()
        assertEquals(allCityLocations[0].city_location, cityLocation.city_location)
    }

    @Test
    @Throws(Exception::class)
    fun getAllCityLocations() = runBlocking {
        val cityLocation = CityLocation("aaa")
        cityLocationDao.insert(cityLocation)
        val cityLocation2 = CityLocation("bbb")
        cityLocationDao.insert(cityLocation2)
        val allCityLocations = cityLocationDao.getAlphabetizedCityLocations().first()
        assertEquals(allCityLocations[0].city_location, cityLocation.city_location)
        assertEquals(allCityLocations[1].city_location, cityLocation2.city_location)
    }

    @Test
    @Throws(Exception::class)
    fun deleteAll() = runBlocking {
        val cityLocation = CityLocation("cityLocation")
        cityLocationDao.insert(cityLocation)
        val cityLocation2 = CityLocation("cityLocation2")
        cityLocationDao.insert(cityLocation2)
        cityLocationDao.deleteAll()
        val allCityLocations = cityLocationDao.getAlphabetizedCityLocations().first()
        assertTrue(allCityLocations.isEmpty())
    }
}