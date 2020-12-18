package et.ad.currentwealther

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class LanguageInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request().url
        val newUrl = url.newBuilder()
            .addQueryParameter("lang", "ja")
            .build()
        val request = Request.Builder().url(newUrl).build()
        return chain.proceed(request)
    }
}