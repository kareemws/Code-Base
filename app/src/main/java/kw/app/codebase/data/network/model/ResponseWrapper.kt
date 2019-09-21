package kw.app.codebase.data.network.model

/**
 * @author Kareem Waleed Sayed
 * @since 20-Sep-19
 *
 * Last Update 20-Sep-19
 *
 * Every network response is eventually contained in a [ResponseWrapper].
 * The [ResponseWrapper] instance consists of 3 parts:
 * The status code mapped to the [Status] enum class values.
 * The json response mapped to one of the business models in case of 2xx type responses (SUCCESS).
 * The json error contained in an [Error] class with a map of "key" causes and clarification "value"
 * in case of response types other than 2xx.
 */

data class ResponseWrapper<ResultType> private constructor(
    var status: Status,
    var data: ResultType? = null,
    var error: Error? = null
) {

    companion object {

        /**
         * Creates [ResponseWrapper] instance with "LOADING" status
         */
        fun <ResultType> loading(): ResponseWrapper<ResultType> =
            ResponseWrapper(Status.LOADING)

        /**
         * Creates [ResponseWrapper] object with `SUCCESS` status and [data].
         */
        fun <ResultType> success(data: ResultType): ResponseWrapper<ResultType> =
            ResponseWrapper(Status.SUCCESS, data)

        /**
         * Creates a [ResponseWrapper] instance that represents "SUCCESS" with no [data]
         * the "NO_CONTENT" cases of response.
         */
        fun <ResultType> noContent(): ResponseWrapper<ResultType> = ResponseWrapper(Status.NO_CONTENT)


        /**
         * Creates a [ResponseWrapper] instance that represented the "UNAUTHORIZED" state in case
         * of authorization failure.
         */
        fun <ResultType> unauthorized(error: Error): ResponseWrapper<ResultType> =
            ResponseWrapper(Status.UNAUTHORIZED, error = error)

        /**
         * Creates [ResponseWrapper] instance with "VALIDATION_ERROR" status and [error]
         * In other words, the servers responded with 422
         */
        fun <ResultType> validationError(error: Error): ResponseWrapper<ResultType> =
            ResponseWrapper(
                Status.VALIDATION_ERROR,
                error = error
            )

        /**
         * Creates an instance of [ResponseWrapper] with no [data] or [error], just a status of
         * "INTERNAL_SERVER_ERROR"
         */
        fun <ResultType> internalServerError(): ResponseWrapper<ResultType> =
            ResponseWrapper(Status.INTERNAL_SERVER_ERROR)

        /**
         * Creates [ResponseWrapper] instance with no [data] or [error], just a status of
         * "FAILED_TO_CONNECT". This status is only achieved through the failure of connection
         * of retrofit client itself
         */
        fun <ResultType> failedToConnect(): ResponseWrapper<ResultType> =
            ResponseWrapper(Status.FAILED_TO_CONNECT)
    }
}