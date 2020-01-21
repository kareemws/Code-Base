package kw.base.module.signal.model

import androidx.collection.ArraySet

sealed class Signal(val signature: String, val flags: ArraySet<Int> = ArraySet()) {
    companion object {
        const val FLAG_REQUIRES_LOADING = 0
        const val FLAG_CAUSES_NAVIGATION = 1
        const val FLAG_STOPS_LOADING_BEFORE = 2
        const val FLAG_STOPS_LOADING_AFTER = 3
        const val FLAG_IS_LAST_IN_SEQUENCE = 4
    }
}

class Load(signature: String) : Signal(signature)
class StopLoading(signature: String, flags: ArraySet<Int> = ArraySet()) : Signal(signature, flags)
class SitIdle(signature: String, flags: ArraySet<Int> = ArraySet()) : Signal(signature, flags)
abstract class CustomSignal(signature: String, flags: ArraySet<Int> = ArraySet()) :
    Signal(signature, flags)