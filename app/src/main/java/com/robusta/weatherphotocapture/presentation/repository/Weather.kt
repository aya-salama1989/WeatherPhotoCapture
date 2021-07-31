package com.robusta.weatherphotocapture.presentation.repository

import com.robusta.weatherphotocapture.networking.WeatherAPIResponse

data class Weather(val temp:String, val conditions:String)

fun WeatherAPIResponse.toEntity() = Weather(temp = "${main.temp} f",
    conditions = "Humidity levels are ${main.humidity}  and feels like ${main.feelsLike}")
