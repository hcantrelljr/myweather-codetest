package com.codetest.myweather.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [CityLocation::class], version = 1, exportSchema = false)
abstract class CityLocationRoomDatabase : RoomDatabase() {

    abstract fun cityLocationDao(): CityLocationDao

    companion object {
        @Volatile
        private var INSTANCE: CityLocationRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): CityLocationRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CityLocationRoomDatabase::class.java,
                    "city_location_database"
                )
                    // Wipes and rebuilds instead of migrating if no Migration object.
                    // Migration is not part of this codelab.
                    .fallbackToDestructiveMigration()
                    .addCallback(CityLocationDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

        private class CityLocationDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            /**
             * Override the onCreate method to populate the database.
             */
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // If you want to keep the data through app restarts,
                // comment out the following line.
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.cityLocationDao())
                    }
                }
            }
        }

        /**
         * Populate the database in a new coroutine.
         * If you want to start with more city locations, just add them.
         */
        suspend fun populateDatabase(cityLocationDao: CityLocationDao) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
            cityLocationDao.deleteAll()

            //var cityLocation = CityLocation("Celina, TX")
            //cityLocationDao.insert(cityLocation)
            //cityLocation = CityLocation("Dallas, TX")
            //cityLocationDao.insert(cityLocation)
        }
    }
}
