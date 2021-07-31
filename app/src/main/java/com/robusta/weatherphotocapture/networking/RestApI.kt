package com.robusta.weatherphotocapture.networking

import com.robusta.weatherphotocapture.networking.RetrofitBuilder.retrofit
import retrofit2.http.GET
import retrofit2.http.Query

interface RestApIService {
    @GET("/data/2.5/weather")
    suspend fun listRepos(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String
    ): WeatherAPIResponse
}


class WeatherAPI {
    val retrofitService: RestApIService by lazy {
        retrofit.create(RestApIService::class.java)
    }
}