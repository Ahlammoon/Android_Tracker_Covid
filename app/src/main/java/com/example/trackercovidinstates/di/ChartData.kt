package com.example.trackercovidinstates

enum class  DataArray{
    NEGATIVE,POSITIVE,HOSPITALIZED,DEATH
}

enum class Time_Scale(val numDays:Int){

    WEEK(7),
    MONTH(30),
    MAX(-1)
}