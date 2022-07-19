package com.codetest.myweather

data class CityWeatherModel(
    val today: WeatherModel,
    val forecast: List<WeatherModel>
)
