package kw.app.codebase.data.network.model

/**
 * @author Kareem Waleed Sayed
 * @since 20-Sep-19
 *
 * Last Update 20-Sep-19
 *
 * Based on the back-end conventions for status code responses, the class
 * in being changed to match them.
 */
enum class Status {

    LOADING,
    SUCCESS,
    NO_CONTENT,
    UNAUTHORIZED,
    VALIDATION_ERROR,
    INTERNAL_SERVER_ERROR,
    FAILED_TO_CONNECT;
}