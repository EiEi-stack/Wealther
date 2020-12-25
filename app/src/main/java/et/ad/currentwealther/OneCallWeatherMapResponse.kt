package et.ad.currentwealther

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OneCallWeatherMapResponse(
    @Json(name = "current")
    val currentList: OneCallWeatherMapResponseCurrent,
    @Json(name = "daily")
    val dailyList: List<OneWeatherMapResponseDaily>
)

@JsonClass(generateAdapter = true)
data class OneCallWeatherMapResponseCurrent(
    @Json(name = "dt")
    val dt: String,
    @Json(name = "temp")
    val temp: String,
    @Json(name = "weather")
    val weatherList: List<OneCallWeatherMapResponseWeather>
)

@JsonClass(generateAdapter = true)
data class OneCallWeatherMapResponseWeather(
    @Json(name = "icon")
    val icon: String
)

@JsonClass(generateAdapter = true)
data class OneWeatherMapResponseDaily(
    @Json(name = "dt")
    val dt: String,
    @Json(name = "temp")
    val temp: OpenWeatherMapResponseTemp,
    @Json(name = "weather")
    val weatherList: List<OneCallWeatherMapResponseWeather>
)

@JsonClass(generateAdapter = true)
data class OpenWeatherMapResponseTemp(
    @Json(name = "min")
    val min: String,
    @Json(name = "max")
    val max: String
)
