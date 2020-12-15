package et.ad.currentwealther


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OpenWeatherMapWeeklyResponse(
@Json(name="list")
val weatherAllList: List<OpenWeatherMapWeeklyResponseList>
)


@JsonClass(generateAdapter = true)
data class OpenWeatherMapWeeklyResponseList(
    @Json(name = "weather")
    val weatherList: List<OpenWeatherMapWeeklyResponseWeather>,
    @Json(name = "main")
    val main: OpenWeatherMapWeeklyResponseMain,
    @Json(name = "name")
    val name: String,
    @Json(name = "dt_txt")
    val dt_txt: String
)

@JsonClass(generateAdapter = true)
data class OpenWeatherMapWeeklyResponseWeather(
    @Json(name = "icon")
    val icon: String)

@JsonClass(generateAdapter = true)
data class OpenWeatherMapWeeklyResponseMain(
    @Json(name = "temp")
    val temp: String
)