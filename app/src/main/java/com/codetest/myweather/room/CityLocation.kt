package com.codetest.myweather.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "city_location_table")
data class CityLocation (
    @PrimaryKey
    @ColumnInfo(name = "city_location")
    val city_location: String,

    @ColumnInfo(name = "latitude")
    val latitude: Double,

    @ColumnInfo(name = "longitude")
    val longitude: Double
): Serializable
