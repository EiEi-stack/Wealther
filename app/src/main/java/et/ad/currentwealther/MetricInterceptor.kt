package et.ad.currentwealther

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class MetricInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request().url
        val newUrl = url.newBuilder()
            .addQueryParameter("units", "metric")
            .build()
        val request = Request.Builder().url(newUrl).build()
        return chain.proceed(request)
    }
}