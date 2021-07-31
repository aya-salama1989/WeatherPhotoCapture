package com.robusta.weatherphotocapture.presentation.datasource

import com.robusta.weatherphotocapture.networking.WeatherAPI
import com.robusta.weatherphotocapture.networking.WeatherAPIResponse

/**
 * WeatherDataSource class
 */

class WeatherDataSource {
    suspend fun getWeatherData(cityName: String): WeatherAPIResponse {
        val weatherAPI = WeatherAPI()
        return weatherAPI.retrofitService.listRepos(cityName, "62db48c28406207c7acb8653397b5994")
    }

}