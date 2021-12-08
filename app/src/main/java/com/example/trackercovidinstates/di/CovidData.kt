package com.example.trackercovidinstates.di

import com.google.gson.annotations.SerializedName
import java.util.*

data class CovidData(

     val dateChecked: Date,
     val positiveIncrease: Int,
     val negativeIncrease: Int,
     val hospitalizedIncrease: Int,
     val deathIncrease: Int,
     val state: String


)