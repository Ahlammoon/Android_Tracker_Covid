package com.example.trackercovidinstates

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.ColorInt
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
//import com.example.trackercovidinstates.databinding
import com.example.trackercovidinstates.databinding.ActivityMainBinding
import com.example.trackercovidinstates.di.CovidData
import com.example.trackercovidinstates.network.RetrofitBuilder.covidService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.text.SimpleDateFormat

////////////////////////

private const val TAG="MainActivity"



class MainActivity : AppCompatActivity() {


    private lateinit var adapter: CovidSparkAdapter
    private lateinit var bindig: ActivityMainBinding

    private lateinit var perStatesDailyData: Map<String, List<CovidData>>
    private lateinit var usDailyData:List<CovidData>
    private lateinit var currentlyShownData: List<CovidData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindig = ActivityMainBinding.inflate(layoutInflater)
        val view: ConstraintLayout = bindig.root

        setContentView(view)


        covidService.getUSData().enqueue(object: Callback<List<CovidData>>{
            override fun onResponse(
                call: Call<List<CovidData>>,
                response: Response<List<CovidData>>
            ) {


                Log.i(TAG,"OnResponse $response")
                val usData = response.body()
                if(usData == null){

                    Log.w(TAG,"Sorry there are not  valid Data")
                    return
                }

                setupEventListeners()
                usDailyData = usData.reversed()
                updateDisplayWithData(  usDailyData )
            }

            override fun onFailure(call: Call<List<CovidData>>, t: Throwable) {

                Log.e(TAG,"On Failure $t")
            }

        })

        //Get the US Data


     // Fetch the state data

        covidService.getStatesData().enqueue(object: Callback<List<CovidData>>{
            override fun onResponse(
                call: Call<List<CovidData>>,
                response: Response<List<CovidData>>
            ) {


                Log.i(TAG,"OnResponse $response")
                val statesData = response.body()
                if(statesData == null){

                    Log.w(TAG,"Sorry there are not  valid Data")
                    return
                }
                perStatesDailyData = statesData.reversed().groupBy { it.state }
                Log.i(TAG,"Update graph with national data")
            //    updateSpinnerWithStateData(perStatesDailyData.keys)
            }

            override fun onFailure(call: Call<List<CovidData>>, t: Throwable) {
                 Log.e(TAG,"On Failure $t")
            }

        })

    }



    private fun setupEventListeners() {
        // addlister
        bindig.sparkView.isScrubEnabled = true
        bindig.sparkView.setScrubListener { itemData ->
            if (itemData is CovidData){
                updataInfoForDate(itemData)
            }
        }


        // Respond to radio button selected events
        bindig.radioGroupTimeSelection.setOnCheckedChangeListener { _, checkedId ->
            adapter.daysAgo = when (checkedId) {
                R.id.rbWeek -> Time_Scale.WEEK
                R.id.rbMonth ->Time_Scale.MONTH
                else -> Time_Scale.MAX
            }
            // Display the last day of the metric
         //   updataInfoForDate(currentlyShownData.last())
            adapter.notifyDataSetChanged()
        }
        //tickerView.setCharacterLists(TickerUtils.provideNumberList())
        // radio button

       bindig.radioGroupMetricSelection.setOnCheckedChangeListener{ _, checkedId ->
            when (checkedId){
                R.id.rbHospitalized -> updataDisplayMetric(DataArray.HOSPITALIZED)
                R.id.rbPositive ->updataDisplayMetric(DataArray.POSITIVE)
                R.id.rbNegative ->updataDisplayMetric(DataArray.NEGATIVE)
                R.id.rbDeath-> updataDisplayMetric(DataArray.DEATH)



            }           
        }

    }

    private fun updataDisplayMetric(metric: DataArray) {

        val colorRes =when (metric){
            DataArray.HOSPITALIZED -> R.color.colorHospitalized
            DataArray.NEGATIVE -> R.color.colorNegative
            DataArray.POSITIVE -> R.color.colorPositive
            DataArray.DEATH -> R.color.colorDeath



                    }



       @ColorInt val  colorInt = ContextCompat.getColor(this,colorRes)
       bindig.sparkView.lineColor = colorInt
        //update the metric on the adapter


        adapter.dataArray = metric
        adapter.notifyDataSetChanged()

        // Rest number and data shown in the bottom text views
         updataInfoForDate(currentlyShownData.last())
    }


    private fun updateDisplayWithData(dailyData: List<CovidData>) {

        currentlyShownData = dailyData
        // Create a new SparkAdapter with the data
        adapter = CovidSparkAdapter(dailyData)
        bindig.sparkView.adapter = adapter
        // Update radio buttons to select positive cases and max time by default
      //  bindig.radioButtonPositive.isChecked = true
        bindig.rbHospitalized.isChecked = true
        bindig.rbMax.isChecked = true
        updataDisplayMetric(DataArray.HOSPITALIZED)
    }

    private fun updataInfoForDate(covidDate: CovidData) {

        val numCases = when (adapter.dataArray)
        {
            DataArray.HOSPITALIZED -> covidDate.hospitalizedIncrease
            DataArray.POSITIVE ->covidDate.positiveIncrease
            DataArray.NEGATIVE -> covidDate.negativeIncrease
            DataArray.DEATH -> covidDate.deathIncrease
        }
        bindig .tvData.text = NumberFormat.getInstance().format(numCases)

        val outputDate= SimpleDateFormat("MMM dd,yyyy")
        bindig .tvDateLabel.text = outputDate.format(covidDate.dateChecked)

    }
}