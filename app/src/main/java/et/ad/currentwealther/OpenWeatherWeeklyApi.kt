package et.ad.currentwealther

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherWeeklyApi {
    @GET("forecast")
    fun getWeeklyReport(
        @Query("q") q: String
    ):Call<OpenWeatherMapWeeklyResponse>
}
