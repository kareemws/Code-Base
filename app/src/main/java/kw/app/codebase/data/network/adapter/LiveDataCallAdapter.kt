package kw.app.codebase.data.network.adapter


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kw.app.codebase.data.network.model.ResponseWrapper
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type

/**
 * @author Kareem Waleed Sayed
 * @since 20-Sep-19
 *
 * Last Update 21-Sep-19
 * A Retrofit adapter that converts the Call into a [LiveData] of [ResponseWrapper].
 * @param <R>
 */
class LiveDataCallAdapter<R>(private val responseType: Type) :
    CallAdapter<R, LiveData<ResponseWrapper<R>>> {

    override fun responseType(): Type {
        return responseType
    }

    override fun adapt(call: Call<R>): LiveData<ResponseWrapper<R>> {
        val liveData = MutableLiveData<ResponseWrapper<R>>()
        call.enqueue(object : Callback<R> {
            override fun onFailure(call: Call<R>, t: Throwable) {
                Log.d("LDAdapter", "${t.message}")
                liveData.postValue(ResponseWrapper.failedToConnect())
            }

            override fun onResponse(call: Call<R>, response: Response<R>) {
                liveData.postValue(response.toResource())
            }

        })
        return liveData
    }
}
