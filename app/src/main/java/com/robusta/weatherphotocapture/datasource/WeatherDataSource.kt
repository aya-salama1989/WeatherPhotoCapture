package com.robusta.weatherphotocapture.datasource

import com.robusta.weatherphotocapture.networking.WeatherAPI
import com.robusta.weatherphotocapture.networking.WeatherAPIResponse

class WeatherDataSource {

    suspend fun getWeatherData(cityId: String): WeatherAPIResponse {
        val weatherAPI = WeatherAPI()
        return weatherAPI.retrofitService.listRepos(cityId, "62db48c28406207c7acb8653397b5994")
    }

}