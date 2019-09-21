package kw.app.codebase.utility

import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


/**
 * @author Kareem Waleed Sayed
 * @since 20-Sep-19
 *
 * Last Update 21-Sep-19
 */

private const val DATE_FORMAT = "dd-MM-yyyy"

abstract class DateTokenizer {
    companion object {
        /**
         * Converts a [date] string to either view-human readable format
         * or back-end database format
         */
        fun convert(date: String, toViewFormat: Boolean): String {
            if (date == "")
                return ""
            val dateTokens = tokenize(date, !toViewFormat)
            return when (toViewFormat) {
                true -> "${dateTokens[DateToken.DAY]}-${dateTokens[DateToken.MONTH]}-${dateTokens[DateToken.YEAR]}"
                false -> "${dateTokens[DateToken.YEAR]}-${dateTokens[DateToken.MONTH]}-${dateTokens[DateToken.DAY]}"
            }
        }

        /**
         * Converts the date of the calendar object to date string
         */
        fun formatCalendarToDateText(date: Calendar): String {
            return SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).format(date.time)
        }

        /**
         * Tokenize the date string to [DateToken.DAY], [DateToken.MONTH] and [DateToken.YEAR].
         * The function is privately used by the [convert] method
         */
        private fun tokenize(date: String, isViewFormat: Boolean): Map<DateToken, Int> {
            val tokensMap = HashMap<DateToken, Int>()
            if (date == "") {
                return tokensMap
            }
            val tokens = date.split("-")
            when (isViewFormat) {
                true -> {
                    tokensMap[DateToken.DAY] = tokens[0].toInt()
                    tokensMap[DateToken.MONTH] = tokens[1].toInt()
                    tokensMap[DateToken.YEAR] = tokens[2].toInt()
                }
                false -> {
                    tokensMap[DateToken.DAY] = tokens[2].toInt()
                    tokensMap[DateToken.MONTH] = tokens[1].toInt()
                    tokensMap[DateToken.YEAR] = tokens[0].toInt()
                }
            }
            return tokensMap
        }
    }
}