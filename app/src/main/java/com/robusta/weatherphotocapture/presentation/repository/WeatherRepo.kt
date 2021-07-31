package com.robusta.weatherphotocapture.presentation.repository

import com.robusta.weatherphotocapture.presentation.datasource.WeatherDataSource

class WeatherRepo {
    suspend fun getWeatherData(cityName: String): Weather {
        return WeatherDataSource().getWeatherData(cityName).toEntity()
    }
}