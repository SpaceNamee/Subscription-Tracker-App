package com.ims.activesubscriptionsapp.data.remote
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import org.json.JSONObject
import retrofit2.Response

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8001/"
    var authToken: String? = null
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
            authToken?.let {
                requestBuilder.addHeader("Authorization", "Bearer $it")
            }

            chain.proceed(requestBuilder.build())
        }
        .build()

    val api: AuthApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApi::class.java)
    }
}

fun <T> Response<T>.getErrorDetail(): String {
    return try {
        val errorBody = this.errorBody()?.string()
        val json = JSONObject(errorBody ?: "")
        json.getString("detail")
    } catch (e: Exception) {
        "Something went wrong"
    }
}
