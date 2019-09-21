package kw.app.codebase.data.network.model

import com.google.gson.JsonParser

/**
 * @author Kareem Waleed Sayed
 * @since 20-Sep-19
 *
 * Last Update 20-Sep-19
 *
 * In case of error responses such as 422 "Validation Error" or any response other than 2xx,
 * the back-end may decide to send json response representing the problem in the "key"
 * and the clarification in a "value".
 * This class, a part of the response mapping process, represents the errors
 * in a map based on their hey and value.
 */
class Error private constructor(val errorMap: Map<String, String?>? = null) {
    companion object {
        fun constructError(errorString: String): Error {
            return Error(parseErrorString(errorString))
        }

        /**
        Parses an errorString in the format of
        {
        "Key":"Value",
        "Key":"Value"
        }
         **/
        private fun parseErrorString(errorString: String): Map<String, String?> {
            val errorJson = JsonParser().parse(errorString).asJsonObject
            val errorMap = mutableMapOf<String, String?>()
            for (key in errorJson.keySet()) {
                if (errorJson[key].isJsonNull) {
                    errorMap[key] = null
                    continue
                }
                errorMap[key] = errorJson.get(key).asString
            }
            return errorMap
        }
    }
}