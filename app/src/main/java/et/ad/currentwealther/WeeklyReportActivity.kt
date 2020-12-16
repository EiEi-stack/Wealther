package et.ad.currentwealther

import android.content.Intent
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

    private val tvWeeklyTempOne by lazy {
        findViewById<TextView>(R.id.tvWeeklyTempOne)
    }

    private val tvWeeklyDateOne by lazy {
        findViewById<TextView>(R.id.tvWeeklyDateOne)
    }

    private val ivWeeklyPicOne by lazy {
        findViewById<ImageView>(R.id.ivWeeklyPicOne)
    }

    private val tvWeeklyTempTwo by lazy {
        findViewById<TextView>(R.id.tvWeeklyTempTwo)
    }

    private val tvWeeklyDateTwo by lazy {
        findViewById<TextView>(R.id.tvWeeklyDateTwo)
    }

    private val ivWeeklyPicTwo by lazy {
        findViewById<ImageView>(R.id.ivWeeklyPicTwo)
    }

    private val tvWeeklyTempThree by lazy {
        findViewById<TextView>(R.id.tvWeeklyTempThree)
    }

    private val tvWeeklyDateThree by lazy {
        findViewById<TextView>(R.id.tvWeeklyDateThree)
    }

    private val ivWeeklyPicThree by lazy {
        findViewById<ImageView>(R.id.ivWeeklyPicThree)
    }

    private val tvWeeklyTempFour by lazy {
        findViewById<TextView>(R.id.tvWeeklyTempFour)
    }

    private val tvWeeklyDateFour by lazy {
        findViewById<TextView>(R.id.tvWeeklyDateFour)
    }

    private val ivWeeklyPicFour by lazy {
        findViewById<ImageView>(R.id.ivWeeklyPicFour)
    }

    private val tvWeeklyTempFive by lazy {
        findViewById<TextView>(R.id.tvWeeklyTempFive)
    }

    private val tvWeeklyDateFive by lazy {
        findViewById<TextView>(R.id.tvWeeklyDateFive)
    }

    private val ivWeeklyPicFive by lazy {
        findViewById<ImageView>(R.id.ivWeeklyPicFive)
    }

    private val tvWeeklyTempSix by lazy {
        findViewById<TextView>(R.id.tvWeeklyTempSix)
    }

    private val tvWeeklyDateSix by lazy {
        findViewById<TextView>(R.id.tvWeeklyDateSix)
    }

    private val ivWeeklyPicSix by lazy {
        findViewById<ImageView>(R.id.ivWeeklyPicSix)
    }
    private val retrofit by lazy {
        RetrofitInstanceFactory.instance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.wealther_layout)
        val intent = intent
        val cityName = intent.getStringExtra("cityName")
        executeNetworkCall(cityName)
    }

    private fun executeNetworkCall(cityName: String) {
        val openWeeklyWeatherMapWeeklyApi = retrofit.create(OpenWeatherMapApi::class.java)
        openWeeklyWeatherMapWeeklyApi.getWeeklyReport(
            q = cityName
        ).enqueue(object : retrofit2.Callback<OpenWeatherMapWeeklyResponse> {
            override fun onFailure(call: Call<OpenWeatherMapWeeklyResponse>, t: Throwable) {
                Log.i("onResponse", "Fail")
            }

            override fun onResponse(
                call: Call<OpenWeatherMapWeeklyResponse>,
                response: Response<OpenWeatherMapWeeklyResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { openWeatherMapWeeklyResponse ->
                        Log.i("onResponse", openWeatherMapWeeklyResponse.toString())
                        tvWeeklyCityName.text = openWeatherMapWeeklyResponse.city.name
                        tvWeeklyTodayTemp.text =
                            openWeatherMapWeeklyResponse.weatherAllList.getOrNull(0)?.main?.temp
                        tvWeeklyTodayDate.text =
                            openWeatherMapWeeklyResponse.weatherAllList.getOrNull(0)!!.dt_txt
                        val iconUrl =
                            openWeatherMapWeeklyResponse.weatherAllList.getOrNull(0)?.weatherList?.getOrNull(
                                0
                            )?.icon ?: ""
                        val fullURL = "https://openweathermap.org/img/wn/$iconUrl@2x.png"
                        Glide.with(this@WeeklyReportActivity).load(fullURL).into(ivWeeklyTodayPic)

                        tvWeeklyTempOne.text =
                            openWeatherMapWeeklyResponse.weatherAllList.getOrNull(1)?.main?.temp
                        tvWeeklyDateOne.text =
                            openWeatherMapWeeklyResponse.weatherAllList.getOrNull(1)!!.dt_txt
                        val iconUrlOne =
                            openWeatherMapWeeklyResponse.weatherAllList.getOrNull(1)?.weatherList?.getOrNull(
                                1
                            )?.icon ?: ""
                        val fullURLOne = "https://openweathermap.org/img/wn/$iconUrlOne@2x.png"
                        Glide.with(this@WeeklyReportActivity).load(fullURLOne).into(ivWeeklyPicOne)

                        tvWeeklyTempTwo.text =
                            openWeatherMapWeeklyResponse.weatherAllList.getOrNull(2)?.main?.temp
                        tvWeeklyDateTwo.text =
                            openWeatherMapWeeklyResponse.weatherAllList.getOrNull(2)!!.dt_txt
                        val iconUrlTwo =
                            openWeatherMapWeeklyResponse.weatherAllList.getOrNull(2)?.weatherList?.getOrNull(
                                2
                            )?.icon ?: ""
                        val fullURLTwo = "https://openweathermap.org/img/wn/$iconUrlTwo@2x.png"
                        Glide.with(this@WeeklyReportActivity).load(fullURLTwo).into(ivWeeklyPicTwo)

                        tvWeeklyTempThree.text =
                            openWeatherMapWeeklyResponse.weatherAllList.getOrNull(3)?.main?.temp
                        tvWeeklyDateThree.text =
                            openWeatherMapWeeklyResponse.weatherAllList.getOrNull(3)!!.dt_txt
                        val iconUrlThree =
                            openWeatherMapWeeklyResponse.weatherAllList.getOrNull(3)?.weatherList?.getOrNull(
                                3
                            )?.icon ?: ""
                        val fullURLThree = "https://openweathermap.org/img/wn/$iconUrlThree@2x.png"
                        Glide.with(this@WeeklyReportActivity).load(fullURLThree).into(ivWeeklyPicThree)

                        tvWeeklyTempFour.text =
                            openWeatherMapWeeklyResponse.weatherAllList.getOrNull(4)?.main?.temp
                        tvWeeklyDateFour.text =
                            openWeatherMapWeeklyResponse.weatherAllList.getOrNull(4)!!.dt_txt
                        val iconUrlFour =
                            openWeatherMapWeeklyResponse.weatherAllList.getOrNull(4)?.weatherList?.getOrNull(
                                4
                            )?.icon ?: ""
                        val fullURLFour = "https://openweathermap.org/img/wn/$iconUrlFour@2x.png"
                        Glide.with(this@WeeklyReportActivity).load(fullURLFour).into(ivWeeklyPicFour)

                        tvWeeklyTempFive.text =
                            openWeatherMapWeeklyResponse.weatherAllList.getOrNull(5)?.main?.temp
                        tvWeeklyDateFive.text =
                            openWeatherMapWeeklyResponse.weatherAllList.getOrNull(5)!!.dt_txt
                        val iconUrlFive =
                            openWeatherMapWeeklyResponse.weatherAllList.getOrNull(5)?.weatherList?.getOrNull(
                                5
                            )?.icon ?: ""
                        val fullURLFive = "https://openweathermap.org/img/wn/$iconUrlFive@2x.png"
                        Glide.with(this@WeeklyReportActivity).load(fullURLFive).into(ivWeeklyPicFive)

                        tvWeeklyTempSix.text =
                            openWeatherMapWeeklyResponse.weatherAllList.getOrNull(6)?.main?.temp
                        tvWeeklyDateSix.text =
                            openWeatherMapWeeklyResponse.weatherAllList.getOrNull(6)!!.dt_txt
                        val iconUrlSix =
                            openWeatherMapWeeklyResponse.weatherAllList.getOrNull(6)?.weatherList?.getOrNull(
                                6
                            )?.icon ?: ""
                        val fullURLSix = "https://openweathermap.org/img/wn/$iconUrlFive@2x.png"
                        Glide.with(this@WeeklyReportActivity).load(fullURLSix).into(ivWeeklyPicSix)
                    }
                }
            }


        })
    }

//    private fun showData(
//        temperature: String,
//        cityName: String,
//        todayDate: String,
//        weatherIcon: String
//    ) {
//        tvWeeklyTodayTemp.text = "$temperature°C"
//        tvWeeklyCityName.text = cityName
//        tvWeeklyTodayDate.text = todayDate
//        Glide.with(this).load(weatherIcon).into(ivWeeklyTodayPic)
//    }
}


