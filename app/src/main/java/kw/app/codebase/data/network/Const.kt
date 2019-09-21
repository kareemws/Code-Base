package kw.app.codebase.data.network


/**
 * @author Kareem Waleed Sayed
 * @since 20-Sep-19
 *
 * Last Update 21-Sep-19
 *
 * A singleton class for network related constant values.
 */
object Const {
    private const val DEFAULT_SERVER_ADDRESS = "https://server_address"
    const val SERVICE_PREFIX = "$DEFAULT_SERVER_ADDRESS/api/"
    const val IMAGE_PREFIX = "$DEFAULT_SERVER_ADDRESS/"
    const val REQUEST_TIMEOUT = 60L

    const val HEADER_KEY_ACCEPT = "Accept"
    const val HEADER_KEY_CONTENT_TYPE = "Content-Type"
    const val HEADER_KEY_AUTHORIZATION = "Authorization"

    const val HEADER_VALUE_APPLICATION_JSON = "application/json"
    const val HEADER_VALUE_BEARER = "Bearer"
}