package et.ad.currentwealther

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OpenWeatherMapResponse(
    @Json(name="coord")
    val coord: OpenWeatherMapResponseCoord,
    @Json(name = "weather")
    val weatherList: List<OpenWeatherMapResponseWeather>,
    @Json(name = "main")
    val main: OpenWeatherMapResponseMain,
    @Json(name = "name")
    val name: String
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

@JsonClass(generateAdapter = true)
data class OpenWeatherMapResponseCoord(
    @Json(name="lon")
    val lon:String,
    @Json(name="lat")
    val lat:String
)