package et.ad.currentwealther

import android.annotation.SuppressLint
import android.content.Context
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
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MainActivity : AppCompatActivity() {
    companion object {
        private const val RESULT_CODE_PERMISSION = 100
        private const val API_KEY = "560c6f6a649feaea5ddf017d0fa2fd39"
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
        findViewById<EditText>(R.id.tvCityName)
    }
    private val btnSearch by lazy {
        findViewById<Button>(R.id.btnSearch)
    }
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/data/2.5/")
        .addConverterFactory(MoshiConverterFactory.create())
        .client(OkHttpClient())
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        Dexter.withActivity(this)
////            .withPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
////            .withListener(object:PermissionListener{
////                @SuppressLint("MissingPermission")
////                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
////                    Log.i("MainActivity.OnCreate","permission granted")
////                    val locationManager = this@MainActivity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
////                    val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
////                    Log.i("MainActivity.OnCreate",location?.latitude.toString())
////                }
////
////                override fun onPermissionRationaleShouldBeShown(
////                    p0: PermissionRequest?,
////                    p1: PermissionToken?
////                ) {
////
////                    Log.i("MainActivity.OnCreate","Permission Shown")
////                }
////
////                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
////
////                    Log.i("MainActivity.OnCreate","Permission denied")
////                }
////
////            }).check()
////        setContentView(R.layout.activity_main))

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
            getLocation()
        }
        //JsonPure
//        val json = "{\"name\": \"Ei Ei Han\",\"phno\": 12345}"
//
//        val jsonObject =JSONObject(json)
//        val name = jsonObject.getString("name")
//        Log.i("MainActivity.json",name)
//        val phNumber = jsonObject.getInt("phno")
//        Log.i("MainActivity.json",phNumber.toString()))

        btnSearch.setOnClickListener {
            val cityName = etCityName.text.toString()
            executeNetworkCall(cityName)
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        val locationManager =
            this@MainActivity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        Log.i("MainActivity.OnCreate", location?.latitude.toString())
        executeNetworkCall(
            latitude = location?.latitude.toString(),
            longitude = location?.longitude.toString()
        )
    }

    private fun executeNetworkCall(
        latitude: String,
        longitude: String
    ) {
//        val httpUrl =
//            HttpUrl.Builder().scheme("https").host("api.openweathermap.org").addPathSegment("data")
//                .addPathSegment("2.5").addPathSegment("weather").addQueryParameter("lat", "16.8409")
//                .addQueryParameter("lon","96.1735").addQueryParameter("appid", API_KEY).build()

        showLoading()
        val openWeatherMapApi = retrofit.create(OpenWeatherMapApi::class.java)
        openWeatherMapApi.geoCoordinate(
            latitude = "16.8409",
            longitude = "96.1735",
            appId = API_KEY,
            units = "metric"
        ).enqueue(object : retrofit2.Callback<OpenWeatherMapResponse> {
            override fun onFailure(call: retrofit2.Call<OpenWeatherMapResponse>, t: Throwable) {
                t.printStackTrace()
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
                }
            }

        })

//        val client = OkHttpClient()
//        val request = Request.Builder().url(httpUrl).build()
//        client.newCall(request).enqueue(object:Callback{
//            override fun onFailure(call: Call, e: IOException) {
//                e.printStackTrace()
//                Log.i("onResponse", e.printStackTrace().toString())
//            }
//
//            override fun onResponse(call: Call, response: Response) {
//                if(response.isSuccessful){
//                    response?.body?.let { responseBody ->
//                        val jsonString=responseBody.string()
//                        val moshi = Moshi.Builder().build()
//                        val adapter = moshi.adapter(OpenWeatherMapResponse::class.java)
//                        val openWeatherResponse = adapter.fromJson(jsonString)
//                        Log.i("onResponse", openWeatherResponse.toString())
//
//                    }
//
//                }else{
//                    Log.i("onResponse", "Response Fail")
//                }
//            }
//        })
    }

    private fun executeNetworkCall(
        cityName: String
    ) {
        showLoading()
        val openWeatherMapApi = retrofit.create(OpenWeatherMapApi::class.java)
        openWeatherMapApi.getByCityName(
            q = cityName,
            appId = API_KEY,
            units = "metric"
        ).enqueue(object : retrofit2.Callback<OpenWeatherMapResponse> {
            override fun onFailure(call: retrofit2.Call<OpenWeatherMapResponse>, t: Throwable) {
                t.printStackTrace()
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
    }

    private fun showData(
        temperature: String,
        cityName: String,
        weatherIcon: String
    ) {
        tvTemperature.text = "$temperature°C"
        etCityName.setText(cityName)
        Glide.with(this).load(weatherIcon).into(ivWeatherStatus)

        progressBar.visibility = View.GONE
        etCityName.visibility = View.VISIBLE
        tvTemperature.visibility = View.VISIBLE
        ivWeatherStatus.visibility = View.VISIBLE
        btnSearch.visibility = View.VISIBLE
    }
}
