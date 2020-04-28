package kw.module.vvm_eventbus.vm

import android.app.Application
import androidx.collection.ArraySet
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kw.module.vvm_eventbus.model.Load
import kw.module.vvm_eventbus.model.Signal
import kw.module.vvm_eventbus.model.Signal.Companion.FLAG_CAUSES_NAVIGATION
import kw.module.vvm_eventbus.model.Signal.Companion.FLAG_IS_LAST_IN_SEQUENCE
import kw.module.vvm_eventbus.model.Signal.Companion.FLAG_REQUIRES_LOADING
import kw.module.vvm_eventbus.model.Signal.Companion.FLAG_STOPS_LOADING_AFTER
import kw.module.vvm_eventbus.model.Signal.Companion.FLAG_STOPS_LOADING_BEFORE
import kw.module.vvm_eventbus.model.SitIdle
import kw.module.vvm_eventbus.model.StopLoading
import java.util.*

abstract class Base(application: Application) : AndroidViewModel(application) {

    protected val signature: String = this::class.java.name

    private val signalsQueue = LinkedList<Signal>()

    private var isWaitingForAcknowledgement: Boolean = false

    private val _signalsEmitter = MutableLiveData<Signal>()
    val signalsEmitter: LiveData<Signal> = _signalsEmitter

    open fun acknowledgeSignal(signal: Signal) {
        if (signal.flags.contains(FLAG_CAUSES_NAVIGATION)) {
            isWaitingForAcknowledgement = false
            signalsQueue.clear()
            _signalsEmitter.value = SitIdle(signature)
        } else if (signal.signature == signature)
            pollNext()
    }

    protected fun enqueueSignal(signal: Signal) {
        signal.apply {
            if (flags.contains(FLAG_REQUIRES_LOADING))
                signalsQueue.add(Load(signature))
            else if (flags.contains(FLAG_STOPS_LOADING_BEFORE))
                signalsQueue.add(StopLoading(signature))

            signalsQueue.add(this)

            if (flags.contains(FLAG_STOPS_LOADING_AFTER)
                && !flags.contains(FLAG_STOPS_LOADING_BEFORE)
            )
                signalsQueue.add(StopLoading(signature))

            if (flags.contains(FLAG_IS_LAST_IN_SEQUENCE))
                signalsQueue.add(SitIdle(signature))
        }

        if (!isWaitingForAcknowledgement) {
            isWaitingForAcknowledgement = true
            pollNext()
        }
    }

    private fun pollNext() {
        if (signalsQueue.isEmpty()) {
            isWaitingForAcknowledgement = false
        } else
            _signalsEmitter.postValue(signalsQueue.poll())
    }
}