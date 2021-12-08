package com.example.trackercovidinstates.network

import com.example.trackercovidinstates.di.CovidData
import retrofit2.Call
import retrofit2.http.GET

interface ServiceApi {



    @GET("us/daily.json")
    fun getUSData(): Call<List<CovidData>>


    @GET("states/daily.json")
    fun getStatesData():Call<List<CovidData>>
}