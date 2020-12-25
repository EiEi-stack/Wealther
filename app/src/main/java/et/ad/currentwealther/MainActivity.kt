package et.ad.currentwealther

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import java.math.BigDecimal

class MainActivity : AppCompatActivity() {
    companion object {
        private const val RESULT_CODE_PERMISSION = 100
    }

    lateinit var cityName: String
    lateinit var locationManager: LocationManager
    var locationGps: Location? = null
    var locationNetwork: Location? = null
    var latlongLocation: Location? = null
    var hasGPS: Boolean = false
    var hasNetwork: Boolean = false

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
        tvWeeklyReport.setOnClickListener {
            val intent = Intent(applicationContext, WeeklyWeatherActivity::class.java)
            val intentLatLong = getLocationLatLon()
            val mLatitude = intentLatLong!!.latitude.toString()
            val mLongitude = intentLatLong!!.longitude.toString()
            intent.putExtra("locLatitude", mLatitude!!)
            intent.putExtra("locLongitude", mLongitude!!)
            intent.putExtra("cityName", cityName!!)
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
            val localLatLong = getLocationLatLon()
            if (localLatLong != null) {
                executeNetworkCall(
                    latitude = localLatLong.latitude.toString(),
                    longitude = localLatLong.longitude.toString()
                )
            }

        }
    }

    @SuppressLint("MissingPermission")
    fun getLocationLatLon(): Location? {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        hasGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if (hasGPS || hasNetwork) {

            if (hasGPS) {
                Log.d("AndroidLocation", "hasGps")
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    5000,
                    0F,
                    object : LocationListener {
                        override fun onLocationChanged(location: Location?) {
                            if (location != null) {
                                locationGps = location
                            }
                        }

                        override fun onStatusChanged(
                            provider: String?,
                            status: Int,
                            extras: Bundle?
                        ) {
                            TODO("Not yet implemented")
                        }

                        override fun onProviderEnabled(provider: String?) {
                            TODO("Not yet implemented")
                        }

                        override fun onProviderDisabled(provider: String?) {
                            TODO("Not yet implemented")
                        }

                    })

                val localGpsLocation =
                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (localGpsLocation != null) {
                    locationGps = localGpsLocation
                }
            }

            if (hasNetwork) {
                Log.d("AndroidLocation", "hasGps")
                locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    5000,
                    0F,
                    object : LocationListener {
                        override fun onLocationChanged(location: Location?) {
                            if (location != null) {
                                locationNetwork = location
                            }
                        }

                        override fun onStatusChanged(
                            provider: String?,
                            status: Int,
                            extras: Bundle?
                        ) {
                            TODO("Not yet implemented")
                        }

                        override fun onProviderEnabled(provider: String?) {
                            TODO("Not yet implemented")
                        }

                        override fun onProviderDisabled(provider: String?) {
                            TODO("Not yet implemented")
                        }

                    })

                val localNetworkLocation =
                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (localNetworkLocation != null) {
                    locationNetwork = localNetworkLocation
                }
                if (locationGps != null && locationNetwork != null) {
                    if (locationGps!!.accuracy > locationNetwork!!.accuracy) {
                        Log.d("AndroidLocation", "Network latitude" + locationNetwork!!.latitude)
                        Log.d("AndroidLocation", "Network latitude" + locationNetwork!!.longitude)
                        latlongLocation = locationNetwork
                    } else {
                        Log.d("AndroidLocation", "GPS latitude" + locationGps!!.latitude)
                        Log.d("AndroidLocation", "GPS latitude" + locationGps!!.longitude)
                        latlongLocation = locationGps
                    }
                }
            }
        } else {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
        return latlongLocation

    }

    private fun executeNetworkCall(
        latitude: String,
        longitude: String
    ) {
        showLoading()

        val openWeatherMapApi = retrofit.create(OpenWeatherMapApi::class.java)

        openWeatherMapApi.geoCoordinate(
            latitude = latitude,
            longitude = longitude
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
                        cityName = openWeatherMapResponse.name
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
                        this@MainActivity.cityName = openWeatherMapResponse.name
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
        val locTemp = BigDecimal(temperature).toBigInteger()
        tvTemperature.text = "$locTemp°C"
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
