package et.ad.currentwealther

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_weekly_weather.*
import retrofit2.Call
import retrofit2.Response
import java.math.BigDecimal
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatterBuilder
import java.util.*

class WeeklyWeatherActivity : AppCompatActivity() {

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

//    private val tvWeeklyTempMinOne by lazy {
//        findViewById<TextView>(R.id.tvWeeklyTempMinOne)
//    }

    private val tvWeeklyTempMaxOne by lazy {
        findViewById<TextView>(R.id.tvWeeklyTempMaxOne)
    }

    private val tvWeeklyDateOne by lazy {
        findViewById<TextView>(R.id.tvWeeklyDateOne)
    }

    private val ivWeeklyPicOne by lazy {
        findViewById<ImageView>(R.id.ivWeeklyPicOne)
    }

//    private val tvWeeklyTempMinTwo by lazy {
//        findViewById<TextView>(R.id.tvWeeklyTempMinTwo)
//    }
    private val tvWeeklyTempMaxTwo by lazy {
        findViewById<TextView>(R.id.tvWeeklyTempMaxTwo)
    }

    private val tvWeeklyDateTwo by lazy {
        findViewById<TextView>(R.id.tvWeeklyDateTwo)
    }

    private val ivWeeklyPicTwo by lazy {
        findViewById<ImageView>(R.id.ivWeeklyPicTwo)
    }

//    private val tvWeeklyTempMinThree by lazy {
//        findViewById<TextView>(R.id.tvWeeklyTempMinThree)
//    }

    private val tvWeeklyTempMaxThree by lazy {
        findViewById<TextView>(R.id.tvWeeklyTempMaxThree)
    }

    private val tvWeeklyDateThree by lazy {
        findViewById<TextView>(R.id.tvWeeklyDateThree)
    }

    private val ivWeeklyPicThree by lazy {
        findViewById<ImageView>(R.id.ivWeeklyPicThree)
    }

//    private val tvWeeklyTempMinFour by lazy {
//        findViewById<TextView>(R.id.tvWeeklyTempMinFour)
//    }

    private val tvWeeklyTempMaxFour by lazy {
        findViewById<TextView>(R.id.tvWeeklyTempMaxFour)
    }

    private val tvWeeklyDateFour by lazy {
        findViewById<TextView>(R.id.tvWeeklyDateFour)
    }

    private val ivWeeklyPicFour by lazy {
        findViewById<ImageView>(R.id.ivWeeklyPicFour)
    }

//    private val tvWeeklyTempMinFive by lazy {
//        findViewById<TextView>(R.id.tvWeeklyTempMinFive)
//    }

    private val tvWeeklyTempMaxFive by lazy {
        findViewById<TextView>(R.id.tvWeeklyTempMaxFive)
    }

    private val tvWeeklyDateFive by lazy {
        findViewById<TextView>(R.id.tvWeeklyDateFive)
    }

    private val ivWeeklyPicFive by lazy {
        findViewById<ImageView>(R.id.ivWeeklyPicFive)
    }

//    private val tvWeeklyTempMinSix by lazy {
//        findViewById<TextView>(R.id.tvWeeklyTempMinSix)
//    }

    private val tvWeeklyTempMaxSix by lazy {
        findViewById<TextView>(R.id.tvWeeklyTempMaxSix)
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
        setContentView(R.layout.activity_weekly_weather)

            val intent = intent
        val latitude = intent.getStringExtra("locLatitude")
        val longitude = intent.getStringExtra("locLongitude")
        val cityName = intent.getStringExtra("cityName")
        if(cityName!=null){
            tvWeeklyCityName.text=cityName
        }

            if(latitude!=null && longitude!=null){
                executeNetworkCall(latitude,longitude)
            }else{
                Toast.makeText(applicationContext,"Latitude Longitude default value is set",Toast.LENGTH_SHORT).show()
                executeNetworkCall("35.3786","139.5976")
            }

    }

    private fun executeNetworkCall(lati: String, logi: String) {
        val oneCallApi = retrofit.create(OpenWeatherMapApi::class.java)
        oneCallApi.getWeeklyWeatherReport(
            lat= lati,
            lon = logi
        ).enqueue(object : retrofit2.Callback<OneCallWeatherMapResponse>{
            override fun onFailure(call: Call<OneCallWeatherMapResponse>, t: Throwable) {
                Log.i("executeNetworkCall","Fail")
            }

            override fun onResponse(
                call: Call<OneCallWeatherMapResponse>,
                response: Response<OneCallWeatherMapResponse>
            ) {
                if(response.isSuccessful){
                    response.body()?.let { oneCallWeatherMapResponse ->
                        Log.i("onResponse",oneCallWeatherMapResponse.toString())
                        setTodayInfo(oneCallWeatherMapResponse)
                        setDayOneInfo(oneCallWeatherMapResponse)
                        setDayTwoInfo(oneCallWeatherMapResponse)
                        setDayThreeInfo(oneCallWeatherMapResponse)
                        setDayFourInfo(oneCallWeatherMapResponse)
                        setDayFiveInfo(oneCallWeatherMapResponse)
                        setDaySixInfo(oneCallWeatherMapResponse)
                    }
                }
            }

        })

    }

    private fun setDaySixInfo(oneCallWeatherMapResponse: OneCallWeatherMapResponse) {
        val tempMax = oneCallWeatherMapResponse.dailyList[6].temp.max
        val bigDecimalMax = BigDecimal(tempMax).toBigInteger()
        tvWeeklyTempMaxSix.text = "$bigDecimalMax℃"
        val tempMin = oneCallWeatherMapResponse.dailyList[6].temp.min
        val bigDecimalMin = BigDecimal(tempMax).toBigInteger()
//        tvWeeklyTempMinSix.text = "$bigDecimalMin℃"

        val unixSeconds= oneCallWeatherMapResponse.dailyList[6].dt
        val sdf= SimpleDateFormat("dd(E)",Locale.JAPAN)
        val currentTimeZone= Date(unixSeconds.toLong()*1000)
        val dt=sdf.format(currentTimeZone)
        tvWeeklyDateSix.text=dt
        val iconUrl=oneCallWeatherMapResponse.dailyList[6].weatherList[0].icon
        val fullUrl = "https://openweathermap.org/img/wn/$iconUrl@2x.png"
        Glide.with(this@WeeklyWeatherActivity).load(fullUrl).into(ivWeeklyPicSix)
    }

    private fun setDayFiveInfo(oneCallWeatherMapResponse: OneCallWeatherMapResponse) {
        val tempMax = oneCallWeatherMapResponse.dailyList[5].temp.max
        val bigDecimalMax = BigDecimal(tempMax).toBigInteger()
        tvWeeklyTempMaxFive.text = "$bigDecimalMax℃"
        val tempMin = oneCallWeatherMapResponse.dailyList[5].temp.min
        val bigDecimalMin = BigDecimal(tempMax).toBigInteger()
//        tvWeeklyTempMinFive.text = "$bigDecimalMin℃"

        val unixSeconds= oneCallWeatherMapResponse.dailyList[5].dt
        val sdf= SimpleDateFormat("dd(E)",Locale.JAPAN)
        val currentTimeZone= Date(unixSeconds.toLong()*1000)
        val dt=sdf.format(currentTimeZone)
        tvWeeklyDateFive.text=dt
        val iconUrl=oneCallWeatherMapResponse.dailyList[5].weatherList[0].icon
        val fullUrl = "https://openweathermap.org/img/wn/$iconUrl@2x.png"
        Glide.with(this@WeeklyWeatherActivity).load(fullUrl).into(ivWeeklyPicFive)
    }

    private fun setDayFourInfo(oneCallWeatherMapResponse: OneCallWeatherMapResponse) {
        val tempMax = oneCallWeatherMapResponse.dailyList[4].temp.max
        val bigDecimalMax = BigDecimal(tempMax).toBigInteger()
        tvWeeklyTempMaxFour.text = "$bigDecimalMax℃"
        val tempMin = oneCallWeatherMapResponse.dailyList[4].temp.min
        val bigDecimalMin = BigDecimal(tempMax).toBigInteger()
//        tvWeeklyTempMinFour.text = "$bigDecimalMin℃"

        val unixSeconds= oneCallWeatherMapResponse.dailyList[4].dt
        val sdf= SimpleDateFormat("dd(E)",Locale.JAPAN)
        val currentTimeZone= Date(unixSeconds.toLong()*1000)
        val dt=sdf.format(currentTimeZone)
        tvWeeklyDateFour.text=dt
        val iconUrl=oneCallWeatherMapResponse.dailyList[4].weatherList[0].icon
        val fullUrl = "https://openweathermap.org/img/wn/$iconUrl@2x.png"
        Glide.with(this@WeeklyWeatherActivity).load(fullUrl).into(ivWeeklyPicFour)
    }

    private fun setDayThreeInfo(oneCallWeatherMapResponse: OneCallWeatherMapResponse) {
        val tempMax = oneCallWeatherMapResponse.dailyList[3].temp.max
        val bigDecimalMax = BigDecimal(tempMax).toBigInteger()
        tvWeeklyTempMaxThree.text = "$bigDecimalMax℃"
        val tempMin = oneCallWeatherMapResponse.dailyList[3].temp.min
        val bigDecimalMin = BigDecimal(tempMax).toBigInteger()
//        tvWeeklyTempMinThree.text = "$bigDecimalMin℃"

        val unixSeconds= oneCallWeatherMapResponse.dailyList[3].dt
        val sdf= SimpleDateFormat("dd(E)",Locale.JAPAN)
        val currentTimeZone= Date(unixSeconds.toLong()*1000)
        val dt=sdf.format(currentTimeZone)
        tvWeeklyDateThree.text=dt
        val iconUrl=oneCallWeatherMapResponse.dailyList[3].weatherList[0].icon
        val fullUrl = "https://openweathermap.org/img/wn/$iconUrl@2x.png"
        Glide.with(this@WeeklyWeatherActivity).load(fullUrl).into(ivWeeklyPicThree)
    }

    private fun setDayTwoInfo(oneCallWeatherMapResponse: OneCallWeatherMapResponse) {
        val tempMax = oneCallWeatherMapResponse.dailyList[2].temp.max
        val bigDecimalMax = BigDecimal(tempMax).toBigInteger()
        tvWeeklyTempMaxTwo.text = "$bigDecimalMax℃"
        val tempMin = oneCallWeatherMapResponse.dailyList[2].temp.min
        val bigDecimalMin = BigDecimal(tempMax).toBigInteger()
//        tvWeeklyTempMinTwo.text = "$bigDecimalMin℃"

        val unixSeconds= oneCallWeatherMapResponse.dailyList[2].dt
        val sdf= SimpleDateFormat("dd(E)",Locale.JAPAN)
        val currentTimeZone= Date(unixSeconds.toLong()*1000)
        val dt=sdf.format(currentTimeZone)
        tvWeeklyDateTwo.text=dt
        val iconUrl=oneCallWeatherMapResponse.dailyList[2].weatherList[0].icon
        val fullUrl = "https://openweathermap.org/img/wn/$iconUrl@2x.png"
        Glide.with(this@WeeklyWeatherActivity).load(fullUrl).into(ivWeeklyPicTwo)
    }

    private fun setDayOneInfo(oneCallWeatherMapResponse: OneCallWeatherMapResponse) {
        val tempMax = oneCallWeatherMapResponse.dailyList[1].temp.max
        val bigDecimalMax = BigDecimal(tempMax).toBigInteger()
        tvWeeklyTempMaxOne.text = "$bigDecimalMax℃"
        val tempMin = oneCallWeatherMapResponse.dailyList[1].temp.min
        val bigDecimalMin = BigDecimal(tempMax).toBigInteger()
//        tvWeeklyTempMinOne.text = "$bigDecimalMin℃"

        val unixSeconds= oneCallWeatherMapResponse.dailyList[1].dt
        val sdf= SimpleDateFormat("dd(E)",Locale.JAPAN)
        val currentTimeZone= Date(unixSeconds.toLong()*1000)
        val dt=sdf.format(currentTimeZone)
        tvWeeklyDateOne.text=dt
        val iconUrl=oneCallWeatherMapResponse.dailyList[1].weatherList[0].icon
        val fullUrl = "https://openweathermap.org/img/wn/$iconUrl@2x.png"
        Glide.with(this@WeeklyWeatherActivity).load(fullUrl).into(ivWeeklyPicOne)
    }

    private fun setTodayInfo(oneCallWeatherMapResponse: OneCallWeatherMapResponse) {
        val temp = oneCallWeatherMapResponse.currentList.temp.toDouble()
        val bigDecimal = BigDecimal(temp).toBigInteger()
        tvWeeklyTodayTemp.text = "$bigDecimal℃"
        val unixSeconds= oneCallWeatherMapResponse.currentList.dt
        val sdf= SimpleDateFormat("dd(E)",Locale.JAPAN)
        val currentTimeZone= Date(unixSeconds.toLong()*1000)
        val dt=sdf.format(currentTimeZone)
        tvWeeklyTodayDate.text=dt
        val iconUrl=oneCallWeatherMapResponse.currentList.weatherList[0].icon
        val fullUrl = "https://openweathermap.org/img/wn/$iconUrl@2x.png"
        Glide.with(this@WeeklyWeatherActivity).load(fullUrl).into(ivWeeklyTodayPic)
    }


}
