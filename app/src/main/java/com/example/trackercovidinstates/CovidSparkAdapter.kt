package com.example.trackercovidinstates

import android.graphics.RectF
import com.example.trackercovidinstates.di.CovidData
import com.robinhood.spark.SparkAdapter

  class CovidSparkAdapter(private  val dailyData: List<CovidData>): SparkAdapter() {

      var dataArray = DataArray.HOSPITALIZED
      var daysAgo = Time_Scale.MAX

      override fun getCount() = dailyData.size

      override fun getItem(index: Int) = dailyData[index]

      override fun getY(index: Int): Float {

          val chosenDayData= dailyData[index]
          return  when (dataArray){

              DataArray.POSITIVE ->chosenDayData.positiveIncrease.toFloat()
              DataArray.NEGATIVE -> chosenDayData.negativeIncrease.toFloat()
              DataArray.HOSPITALIZED -> chosenDayData.hospitalizedIncrease.toFloat()
              DataArray.DEATH -> chosenDayData.deathIncrease.toFloat()

          }
      }
     override fun getDataBounds(): RectF {
         val bounds = super.getDataBounds()
         if(daysAgo != Time_Scale.MAX) {
             bounds.left = count - daysAgo.numDays.toFloat()
         }
         return bounds
     }
  }
