package kw.app.codebase.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kw.app.codebase.model.User

/**
 * @author Kareem Waleed Sayed
 * @since 20-Sep-19
 *
 * Last Update 21-Sep-19
 */

private const val USER_PREFERENCE_FILE_NAME = "user"

private const val OTHERS_PREFERENCE_FILE_NAME = "others"

private const val KEY_IS_FIRST_TIME = "is_first_time"

private const val KEY_USER = "user"

object SharedPreferenceUtil {

    private var userSharedPreferencesInstance: SharedPreferences? = null
    private var othersSharedPreferencesInstance: SharedPreferences? = null

    /**
     * To construct the [SharedPreferences] object once we have a context.
     * In Splash screens for example
     */
    fun constructSharedPreference(context: Context) {
        if (userSharedPreferencesInstance == null)
            userSharedPreferencesInstance =
                context.getSharedPreferences(USER_PREFERENCE_FILE_NAME, Context.MODE_PRIVATE)
        if (othersSharedPreferencesInstance == null)
            userSharedPreferencesInstance =
                context.getSharedPreferences(OTHERS_PREFERENCE_FILE_NAME, Context.MODE_PRIVATE)
    }

    /**
     * To handle Wizards for first time users.
     * Throws a [KotlinNullPointerException] if the [SharedPreferences] instance is not constructed
     */
    fun isFirstTime(): Boolean {
        val isFirstTime = othersSharedPreferencesInstance!!.getBoolean(KEY_IS_FIRST_TIME, true)
        if (isFirstTime)
            with(othersSharedPreferencesInstance?.edit()!!) {
                putBoolean(KEY_IS_FIRST_TIME, false)
                apply()
            }
        return isFirstTime
    }

    /**
     * Saves an entire user object as json
     * Throws a [KotlinNullPointerException] if the [SharedPreferences] instance is not constructed
     */
    fun saveUser(user: User) {
        with(userSharedPreferencesInstance!!.edit()) {
            putString(KEY_USER, Gson().toJson(user))
            apply()
        }
    }

    /**
     * Retrieves the user instance
     * Throws a [KotlinNullPointerException] if the [SharedPreferences] instance is not constructed
     */
    fun retrieveUser(): User? {
        val serializedUser = userSharedPreferencesInstance!!.getString(KEY_USER, null)
        val userType = object : TypeToken<User>() {}.type
        return Gson().fromJson(serializedUser, userType)
    }

    /**
     * Clears the user preference file
     * Throws a [KotlinNullPointerException] if the [SharedPreferences] instance is not constructed
     */
    fun deleteUser() {
        userSharedPreferencesInstance!!.edit()?.clear()?.apply()
    }
}