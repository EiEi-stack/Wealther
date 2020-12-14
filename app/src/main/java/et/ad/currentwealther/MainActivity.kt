package et.ad.currentwealther

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import okhttp3.*
import org.w3c.dom.Text
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {
    companion object {
        private const val RESULT_CODE_PERMISSION = 100
    }

    private val progressBar by lazy {
        findViewById<ProgressBar>(R.id.progressBar)
    }
    private val tvTemperature by lazy {
        findViewById<TextView>(R.id.tvTemperature)
    }
    private val ivWeatherStatus by lazy {
        findViewById<ImageView>(R.id.ivWeatherStatus)
    }
    private val etCityName by lazy {
        findViewById<EditText>(R.id.etCityName)
    }
    private val btnSearch by lazy {
        findViewById<Button>(R.id.btnSearch)
    }
    private val retrofit by lazy {
        RetrofitInstanceFactory.instance()
    }
    private val tvError by lazy {
        findViewById<TextView>(R.id.tvError)
    }
    private val btnReset by lazy {
        findViewById<Button>(R.id.btnReset)
    }
    private val tvShowCityName by lazy {
        findViewById<TextView>(R.id.tvCityName)
    }
    private val tvWeeklyReport by lazy {
        findViewById<TextView>(R.id.tvWeeklyReport)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnSearch.setOnClickListener {
            val cityName = etCityName.text.toString()
            executeNetworkCall(cityName)
        }
        btnReset.setOnClickListener {
            getLocation()
        }
        tvWeeklyReport.setOnClickListener{
            val intent = Intent(applicationContext,WeeklyReportActivity::class.java)
            startActivity(intent)
        }

        getLocation()
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), RESULT_CODE_PERMISSION
            )
        } else {
            val locationManager =
                this@MainActivity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val location =
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            Log.i("MainActivity.OnCreate", location?.latitude.toString())
            executeNetworkCall(
                latitude = location?.latitude.toString(),
                longitude = location?.longitude.toString()
            )
        }
    }

    private fun executeNetworkCall(
        latitude: String,
        longitude: String
    ) {
        showLoading()
        val openWeatherMapApi = retrofit.create(OpenWeatherMapApi::class.java)
        openWeatherMapApi.geoCoordinate(
            latitude = "16.871311",
            longitude = "96.199379"
        ).enqueue(object : retrofit2.Callback<OpenWeatherMapResponse> {
            override fun onFailure(call: retrofit2.Call<OpenWeatherMapResponse>, t: Throwable) {
                showError()
            }

            override fun onResponse(
                call: retrofit2.Call<OpenWeatherMapResponse>,
                response: retrofit2.Response<OpenWeatherMapResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { openWeatherMapResponse ->
                        Log.i("onResponse", openWeatherMapResponse.toString())
                        val iconUrl = openWeatherMapResponse.weatherList.getOrNull(0)?.icon ?: ""
                        val fullURL = "https://openweathermap.org/img/wn/$iconUrl@2x.png"
                        showData(
                            temperature = openWeatherMapResponse.main.temp,
                            cityName = openWeatherMapResponse.name,
                            weatherIcon = fullURL
                        )
                    }
                } else {
                    showError()
                }
            }

        })
    }

    private fun executeNetworkCall(
        cityName: String
    ) {
        showLoading()
        val openWeatherMapApi = retrofit.create(OpenWeatherMapApi::class.java)
        openWeatherMapApi.getByCityName(
            q = cityName
        ).enqueue(object : retrofit2.Callback<OpenWeatherMapResponse> {
            override fun onFailure(call: retrofit2.Call<OpenWeatherMapResponse>, t: Throwable) {
                t.printStackTrace()
                showError()
            }

            override fun onResponse(
                call: retrofit2.Call<OpenWeatherMapResponse>,
                response: retrofit2.Response<OpenWeatherMapResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { openWeatherMapResponse ->
                        Log.i("onResponse", openWeatherMapResponse.toString())
                        val iconUrl = openWeatherMapResponse.weatherList.getOrNull(0)?.icon ?: ""
                        val fullURL = "https://openweathermap.org/img/wn/$iconUrl@2x.png"
                        showData(
                            temperature = openWeatherMapResponse.main.temp,
                            cityName = openWeatherMapResponse.name,
                            weatherIcon = fullURL
                        )
                    }
                } else {
                    showError()
                }
            }

        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == RESULT_CODE_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation()
            } else if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Log.i("MainActivity.OnCreate", "Permission Denied")
            }
        }
    }

    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
        etCityName.visibility = View.GONE
        tvTemperature.visibility = View.GONE
        ivWeatherStatus.visibility = View.GONE
        btnSearch.visibility = View.GONE

        tvError.visibility = View.GONE
        btnReset.visibility = View.GONE
        tvShowCityName.visibility = View.GONE
        tvWeeklyReport.visibility = View.GONE
    }

    private fun showData(
        temperature: String,
        cityName: String,
        weatherIcon: String
    ) {
        tvTemperature.text = "$temperature°C"
        etCityName.setText(cityName)
        tvShowCityName.text = cityName
        Glide.with(this).load(weatherIcon).into(ivWeatherStatus)

        progressBar.visibility = View.GONE
        tvError.visibility = View.GONE
        btnReset.visibility = View.GONE
        etCityName.visibility = View.VISIBLE
        tvTemperature.visibility = View.VISIBLE
        tvShowCityName.visibility = View.VISIBLE
        ivWeatherStatus.visibility = View.VISIBLE
        tvWeeklyReport.visibility = View.VISIBLE
        btnSearch.visibility = View.VISIBLE
    }

    private fun showError() {
        progressBar.visibility = View.GONE
        etCityName.visibility = View.GONE
        tvShowCityName.visibility = View.GONE
        tvTemperature.visibility = View.GONE
        ivWeatherStatus.visibility = View.GONE
        btnSearch.visibility = View.GONE
        tvWeeklyReport.visibility = View.GONE

        tvError.visibility = View.VISIBLE
        btnReset.visibility = View.VISIBLE
    }
}
