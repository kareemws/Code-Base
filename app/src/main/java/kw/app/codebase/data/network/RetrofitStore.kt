package kw.app.codebase.data.network

import androidx.lifecycle.Observer
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * @author Kareem Waleed Sayed
 * @since 20-Sep-19
 *
 * Last Update 21-Sep-19
 *
 * A singleton class that is responsible for constructing and reconstructing, with token,
 * the retrofit client with [OkHttpClient]
 */

object RetrofitStore {
    private var retrofitClient = initClient()

    private val subscribers = ArrayList<Observer<Service>>()

    private fun initClient(token: String? = null): Retrofit {

        val httpClient = OkHttpClient().newBuilder()
            .connectTimeout(Const.REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(Const.REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(Const.REQUEST_TIMEOUT, TimeUnit.SECONDS)

        httpClient.addInterceptor(Interceptor.invoke {
            val original = it.request()
            val requestBuilder = original.newBuilder()
                .addHeader(Const.HEADER_KEY_ACCEPT, Const.HEADER_VALUE_APPLICATION_JSON)
                .addHeader(Const.HEADER_KEY_CONTENT_TYPE, Const.HEADER_VALUE_APPLICATION_JSON)
            if (token != null)
                requestBuilder.addHeader(
                    Const.HEADER_KEY_AUTHORIZATION,
                    "${Const.HEADER_VALUE_BEARER} $token"
                )
            val request = requestBuilder.build()
            it.proceed(request)
        })
        return Retrofit.Builder()
            .baseUrl(Const.SERVICE_PREFIX).client(httpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun setToken(token: String?) {
        retrofitClient = initClient(token)
        notifyObservers()
    }

    fun getServiceObject(): Service {
        return retrofitClient.create(Service::class.java)
    }

    fun attachServiceObserver(observer: Observer<Service>) {
        subscribers.add(observer)
    }

    fun detachServiceObserver(observer: Observer<Service>) {
        subscribers.remove(observer)
    }

    private fun notifyObservers() {
        subscribers.forEach {
            it.onChanged(getServiceObject())
        }
    }
}