package com.codetest.myweather

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import kotlin.math.roundToInt

class OpenWeatherUtil {

    private val client = OkHttpClient()

    fun jsonToWeatherModel(jsonData: JSONObject): WeatherModel {
        val jsonMain = jsonData.getJSONObject("main")
        val jsonWeather = jsonData.getJSONArray("weather").getJSONObject(0)
        val jsonWind = jsonData.getJSONObject("wind")

        val temperature = jsonMain.getDouble("temp").roundToInt().toString()
        val humidity = jsonMain.getDouble("humidity").roundToInt().toString()
        val weatherIconUrl = String.format(
            "https://openweathermap.org/img/wn/%s@4x.png",
            jsonWeather.getString("icon")
        )
        val windSpeed = jsonWind.getDouble("speed").roundToInt().toString()

        return WeatherModel(temperature, humidity, weatherIconUrl, windSpeed)
    }

    fun execute(
        latitude: String,
        longitude: String,
        callback: LoadDataCallback<CityWeatherModel>
    ) {
        // Today’s forecast, including: temperature, humidity, rain chances and wind information
        // show the 5-days forecast, including: temperature, humidity, rain chances and wind information.
        // http://api.openweathermap.org/data/2.5/weather?lat=32.7767&lon=-96.7970&units=imperial&appid=fae7190d7e6433ec3a45285ffcf55c86 // metric
        // http://api.openweathermap.org/data/2.5/forecast?lat=32.7767&lon=-96.7970&units=imperial&appid=fae7190d7e6433ec3a45285ffcf55c86
        // temperature = data.temp Temperature. Units – default: kelvin, metric: Celsius, imperial: Fahrenheit.
        // humidity = data.humidity Humidity, %
        // rain chances = data.weather.icon Weather icon id. http://openweathermap.org/img/wn/10d@4x.png
        // wind information = data.wind_speed Wind speed. Units – default: metre/sec, metric: metre/sec, imperial: miles/hour.

        try {
            val units = "imperial"
            val appId = "fae7190d7e6433ec3a45285ffcf55c86"

            val request = Request.Builder()
                .url(String.format("https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&units=%s&appid=%s",
                    latitude,
                    longitude,
                    units,
                    appId))
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    callback.onDataNotAvailable(1, "Something went wrong!")
                }

                val contents = response.body!!.string()
                val jsonData = JSONObject(contents)

                val todayWeather = jsonToWeatherModel(jsonData)

                val requestForecast = Request.Builder()
                    .url(String.format("https://api.openweathermap.org/data/2.5/forecast?lat=%s&lon=%s&units=%s&appid=%s",
                        latitude,
                        longitude,
                        units,
                        appId))
                    .build()

                client.newCall(requestForecast).execute().use { responseForecast ->
                    if (!responseForecast.isSuccessful) {
                        callback.onDataNotAvailable(1, "Something went wrong!")
                    }

                    val contentsForcast = responseForecast.body!!.string()
                    val jsonForecastData = JSONObject(contentsForcast)
                    val jsonList = jsonForecastData.getJSONArray("list")

                    val forecast = mutableListOf<WeatherModel>()

                    for (i in 0..4) {
                        val jsonItem = jsonList.getJSONObject(i)

                        val dayWeather = jsonToWeatherModel(jsonItem)
                        forecast.add(dayWeather)
                    }

                    callback.onDataLoaded(CityWeatherModel(todayWeather, forecast))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            callback.onDataNotAvailable(1, "Something went wrong!")
        }
    }
}
