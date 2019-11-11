package kw.app.codebase.model.gson.deserializer

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import kw.app.codebase.model.gson.TypeAdapter
import kw.app.codebase.utility.DateUtils

abstract class Base<T> : JsonDeserializer<T> {
    protected lateinit var converter: Gson

    protected fun builderConverter(vararg typeAdapters: TypeAdapter) {
        var gsonBuilder = GsonBuilder()
        typeAdapters.forEach { typeAdapter ->
            gsonBuilder = gsonBuilder.registerTypeAdapter(typeAdapter.type, typeAdapter.typeAdapter)
        }
        converter = gsonBuilder.create()
    }

    protected fun deserializeDate(dateString: String?) = DateUtils.convert(dateString, true)

    protected fun deserializeBoolean(booleanInt: Int?): Boolean? =
        booleanInt?.let { assertedBooleanInt ->
            when (assertedBooleanInt) {
                0 -> false
                else -> true
            }
        }
}