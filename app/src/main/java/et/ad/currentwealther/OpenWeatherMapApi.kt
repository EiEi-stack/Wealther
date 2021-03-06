package et.ad.currentwealther

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherMapApi {
    @GET("weather")
    fun geoCoordinate(
        @Query("lat") latitude:String,
        @Query("lon") longitude:String
    ):Call<OpenWeatherMapResponse>

    @GET("weather")
    fun getByCityName(
        @Query("q") q:String
    ):Call<OpenWeatherMapResponse>

    @GET("forecast")
    fun getWeeklyReport(
        @Query("q") q: String
    ):Call<OpenWeatherMapWeeklyResponse>

    @GET("onecall")
    fun getWeeklyWeatherReport(
        @Query("lat") lat: String,
        @Query("lon") lon: String
    ):Call<OneCallWeatherMapResponse>
    }
