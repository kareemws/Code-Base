package kw.app.codebase.view.utility

import androidx.collection.ArraySet

class Signal(val command: Command, val signature: String, val flags: ArraySet<Int>) {
    companion object {
        const val FLAG_REQUIRES_LOADING = 0
        const val FLAG_CAUSES_NAVIGATION = 1
        const val FLAG_STOPS_LOADING_BEFORE = 2
        const val FLAG_STOPS_LOADING_AFTER = 3
        const val FLAG_IS_LAST_IN_SEQUENCE = 4
    }
}