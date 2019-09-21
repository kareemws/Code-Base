package kw.app.codebase.data.network.adapter

import android.util.Log
import kw.app.codebase.data.network.model.Error
import kw.app.codebase.data.network.model.ResponseWrapper
import retrofit2.Response

/**
 * @author Kareem Waleed Sayed
 * @since 20-Sep-19
 *
 * Last Update 21-Sep-19
 *
 * An extension function for retrofit base [Response] class that maps the responses
 * based on the response code to a [ResponseWrapper]
 */
fun <ResultType> Response<ResultType>.toResource(): ResponseWrapper<ResultType> {
    val errorString = errorBody()?.string()
    Log.d("Extension", "Error Body: $errorString")
    Log.d("Extension", "Response Body: ${body()}")
    Log.d("Extension", "Response Code: ${code()}")
    return when (code()) {
        200 -> ResponseWrapper.success(body()!!)
        204 -> ResponseWrapper.noContent()
        401 -> ResponseWrapper.unauthorized(Error.constructError(errorString!!))
        422 -> ResponseWrapper.validationError(Error.constructError(errorString!!))
        else -> ResponseWrapper.internalServerError()
    }
}