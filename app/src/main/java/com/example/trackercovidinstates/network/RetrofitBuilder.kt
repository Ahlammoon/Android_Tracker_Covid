package com.example.trackercovidinstates.network

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {


   // private const val BASE_URL = "https://api.covidtracking.com/v1"
    const val BASE_URL = "https://covidtracking.com/api/v1/"
    val covidService: ServiceApi
    init {
        val gson= GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        covidService = retrofitBuilder.create(ServiceApi::class.java)
    }
}