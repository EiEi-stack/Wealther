package et.ad.currentwealther

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Response

class WeeklyReportActivity : AppCompatActivity() {
    companion object {
        private const val REQUEST_CODE = 100
    }

    private val tvWeeklyCityName by lazy {
        findViewById<TextView>(R.id.tvWeeklyCityName)
    }

    private val tvWeeklyTodayTemp by lazy {
        findViewById<TextView>(R.id.tvWeeklyTodayTemp)
    }

    private val tvWeeklyTodayDate by lazy {
        findViewById<TextView>(R.id.tvWeeklyTodayDate)
    }

    private val ivWeeklyTodayPic by lazy {
        findViewById<ImageView>(R.id.ivWeeklyTodayPic)
    }
    private val retrofit by lazy {
        RetrofitInstanceFactory.instance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.wealther_layout)
        executeNetworkCall("yangon")
    }

    private fun executeNetworkCall(cityName: String) {
        val openWeeklyWeatherMapWeeklyApi = retrofit.create(OpenWeatherWeeklyApi::class.java)
        openWeeklyWeatherMapWeeklyApi.getWeeklyReport(
            q = cityName
        ).enqueue(object :retrofit2.Callback<OpenWeatherMapWeeklyResponse>{
            override fun onFailure(call: Call<OpenWeatherMapWeeklyResponse>, t: Throwable) {
                Log.i("onResponse","Fail")
            }

            override fun onResponse(
                call: Call<OpenWeatherMapWeeklyResponse>,
                response: Response<OpenWeatherMapWeeklyResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { openWeatherMapWeeklyResponse ->
                        Log.i("onResponse", openWeatherMapWeeklyResponse.toString())
                        val iconUrl =
                            openWeatherMapWeeklyResponse.weatherAllList.getOrNull(0)?.weatherList?.getOrNull(0)?.icon ?: ""
                        val fullURL = "https://openweathermap.org/img/wn/$iconUrl@2x.png"
                        openWeatherMapWeeklyResponse.weatherAllList.getOrNull(0)?.main?.temp?.let {
                            openWeatherMapWeeklyResponse.weatherAllList.getOrNull(1)?.name?.let { it1 ->
                                showData(
                                    temperature = it,
                                    cityName = it1,
                                    todayDate = openWeatherMapWeeklyResponse.weatherAllList.getOrNull(0)!!.dt_txt,
                                    weatherIcon = fullURL
                                )
                            }
                        }
                    }
                }
            }
        })
    }

    private fun showData(
        temperature: String,
        cityName: String,
        todayDate: String,
        weatherIcon: String
    ) {
        tvWeeklyTodayTemp.text = "$temperature°C"
        tvWeeklyCityName.text = cityName
        tvWeeklyTodayDate.text = todayDate
        Glide.with(this).load(weatherIcon).into(ivWeeklyTodayPic)
    }
}


