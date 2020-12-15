package et.ad.currentwealther

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OpenWeatherMapResponse(
    @Json(name = "weather")
    val weatherList: List<OpenWeatherMapResponseWeather>,
    @Json(name = "main")
    val main: OpenWeatherMapResponseMain,
    @Json(name = "name")
    val name: String
//    @Json(name = "dt_txt")
//    val dt_txt: String
)

@JsonClass(generateAdapter = true)
data class OpenWeatherMapResponseWeather(
    @Json(name = "icon")
    val icon: String)

@JsonClass(generateAdapter = true)
data class OpenWeatherMapResponseMain(
    @Json(name = "temp")
    val temp: String
)