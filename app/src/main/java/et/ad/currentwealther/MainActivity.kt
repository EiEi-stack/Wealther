package et.ad.currentwealther

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.squareup.moshi.Moshi
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {
    companion object {
        private const val RESULT_CODE_PERMISSION = 100
        private const val API_KEY = "560c6f6a649feaea5ddf017d0fa2fd39"
    }

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
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        val locationManager =
            this@MainActivity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        Log.i("MainActivity.OnCreate", location?.latitude.toString())
        executeNetworkCall(latitude = location?.latitude.toString(),longitude = location?.longitude.toString())
    }

    private fun executeNetworkCall(
        latitude: String,
        longitude: String
    ) {
        val httpUrl =
            HttpUrl.Builder().scheme("https").host("api.openweathermap.org").addPathSegment("data")
                .addPathSegment("2.5").addPathSegment("weather").addQueryParameter("lat", latitude)
                .addQueryParameter("lon",longitude).addQueryParameter("appid", API_KEY).build()

        val client = OkHttpClient()
        val request = Request.Builder().url(httpUrl).build()
        client.newCall(request).enqueue(object:Callback{
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful){
                    response?.body?.let { responseBody ->
                        val jsonString=responseBody.toString()
                        val moshi = Moshi.Builder().build()
                        val adapter = moshi.adapter(OpenWeatherMapResponse::class.java)
                        val openWeatherResponse = adapter.fromJson(jsonString)
                        Log.i("MainActivity.Response", openWeatherResponse.toString())

                    }

                }else{
                    Log.i("MainActivity.Response", "Response Fail")
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
}
