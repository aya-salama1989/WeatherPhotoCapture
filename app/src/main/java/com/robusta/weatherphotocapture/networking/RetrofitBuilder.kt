package com.robusta.weatherphotocapture.networking

import com.robusta.weatherphotocapture.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


private const val BASE_URL = "https://api.openweathermap.org"

object RetrofitBuilder{
     val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(getClient()).build()
}


fun getClient(): OkHttpClient {
    val httpClient = OkHttpClient.Builder()
    if(BuildConfig.DEBUG){// in debug mode show logs
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        httpClient.addInterceptor(logging)
    }
    return  httpClient.build()
}







