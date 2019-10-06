package kw.app.codebase.data.network.model

/**
 * Based on the back-end conventions for status code responses, the class
 * in being changed to match them.
 */
enum class Status {
    SUCCESS,
    NO_CONTENT,
    UNAUTHORIZED,
    FORBIDDEN,
    VALIDATION_ERROR,
    INTERNAL_SERVER_ERROR,
    FAILED_TO_CONNECT;

    companion object {
        fun construct(status: Int?): Status {
            return when (status) {
                200 -> SUCCESS
                204 -> NO_CONTENT
                401 -> UNAUTHORIZED
                403 -> FORBIDDEN
                422 -> VALIDATION_ERROR
                500 -> INTERNAL_SERVER_ERROR
                else -> FAILED_TO_CONNECT
            }
        }
    }
}