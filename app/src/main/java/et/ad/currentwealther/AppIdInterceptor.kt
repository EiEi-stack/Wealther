package et.ad.currentwealther

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AppIdInterceptor: Interceptor {
    companion object{
        private const val API_KEY = "560c6f6a649feaea5ddf017d0fa2fd39"
    }
    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request().url
        val newUrl = url.newBuilder()
        .addQueryParameter("appid",API_KEY)
            .build()
        val request = Request.Builder().url(newUrl).build()
        return chain.proceed(request)
    }
}