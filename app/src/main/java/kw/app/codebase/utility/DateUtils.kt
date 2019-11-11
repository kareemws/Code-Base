package kw.app.codebase.utility

import java.text.SimpleDateFormat
import java.util.*


/**
 * @author Kareem Waleed Sayed
 * @since 20-Sep-19
 *
 * Last Update 21-Sep-19
 */

private const val VIEW_DATE_FORMAT = "dd-MM-yyyy"
private const val REMOTE_DATE_FORMAT = "yyyy-MM-dd"

abstract class DateUtils {

    companion object {
        private val remoteFormatter = SimpleDateFormat(REMOTE_DATE_FORMAT, Locale.ENGLISH)
        private val viewFormatter = SimpleDateFormat(VIEW_DATE_FORMAT, Locale.ENGLISH)

        fun convert(date: Date, isForRemote: Boolean = false): String {
            return if (isForRemote)
                remoteFormatter.format(date)
            else
                viewFormatter.format(date)
        }

        fun convert(date: String?, isFromRemote: Boolean = false): Date? =
            date?.let { assertedDate ->
                return if (isFromRemote)
                    remoteFormatter.parse(assertedDate)
                else
                    viewFormatter.parse(assertedDate)
            }


        fun isEqualDates(fDate: Date?, sDate: Date?): Boolean {
            if (fDate == null || sDate == null)
                return false
            val fCalendar = Calendar.getInstance()
            val sCalendar = Calendar.getInstance()

            fCalendar.time = fDate
            sCalendar.time = sDate

            return fCalendar.get(Calendar.YEAR) == sCalendar.get(Calendar.YEAR)
                    && fCalendar.get(Calendar.MONTH) == sCalendar.get(Calendar.MONTH)
                    && fCalendar.get(Calendar.DAY_OF_MONTH) == sCalendar.get(Calendar.DAY_OF_MONTH)
        }
    }
}