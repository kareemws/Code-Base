package kw.module.vvm_eventbus.model

sealed class Signal(val signature: String, val flags: IntArray = intArrayOf()) {
    companion object {
        const val FLAG_REQUIRES_LOADING = 0
        const val FLAG_CAUSES_NAVIGATION = 1
        const val FLAG_STOPS_LOADING_BEFORE = 2
        const val FLAG_STOPS_LOADING_AFTER = 3
        const val FLAG_IS_LAST_IN_SEQUENCE = 4
    }
}

class Load(signature: String) : Signal(signature)
class StopLoading(signature: String, vararg flags: Int) : Signal(signature, flags)
class SitIdle(signature: String, vararg flags: Int) : Signal(signature, flags)
abstract class CustomSignal(signature: String, flags: IntArray = intArrayOf()) :
    Signal(signature, flags)