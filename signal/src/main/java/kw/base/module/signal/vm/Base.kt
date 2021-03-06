package kw.module.vvm_eventbus.vm

import android.app.Application
import androidx.collection.ArraySet
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.*

abstract class Base(application: Application) : AndroidViewModel(application) {

    protected val signature: String = this::class.java.name

    private val signalsQueue = LinkedList<Signal>()

    private var isWaitingForAcknowledgement: Boolean = false

    private val signalsEmitterMLive = MutableLiveData<Signal>()

    val signalsEmitter: LiveData<Signal> = signalsEmitterMLive

    open fun acknowledgeSignal(signal: Signal) {
        if (signal.flags.contains(FLAG_CAUSES_NAVIGATION)) {
            isWaitingForAcknowledgement = false
            signalsQueue.clear()
            signalsEmitterMLive.value = Signal(SIT_IDLE, signature, ArraySet())
        } else if (signal.signature == signature)
            pollNext()
    }

    protected fun enqueueCommand(command: String, vararg flags: Int) {
        val flagsSet = flags.toCollection(ArraySet())

        if (flagsSet.contains(FLAG_REQUIRES_LOADING))
            signalsQueue.add(Signal(LOAD, signature, ArraySet()))
        else if (flagsSet.contains(FLAG_STOPS_LOADING_BEFORE))
            signalsQueue.add(Signal(STOP_LOADING, signature, ArraySet()))

        signalsQueue.add(Signal(command, signature, flagsSet))

        if (flagsSet.contains(FLAG_STOPS_LOADING_AFTER)
            && !flagsSet.contains(FLAG_STOPS_LOADING_BEFORE)
        )
            signalsQueue.add(Signal(STOP_LOADING, signature, ArraySet()))

        if (flagsSet.contains(FLAG_IS_LAST_IN_SEQUENCE))
            signalsQueue.add(Signal(SIT_IDLE, signature, ArraySet()))

        if (!isWaitingForAcknowledgement) {
            isWaitingForAcknowledgement = true
            pollNext()
        }
    }

    private fun pollNext() {
        if (signalsQueue.isEmpty()) {
            isWaitingForAcknowledgement = false
        } else
            signalsEmitterMLive.postValue(signalsQueue.poll())
    }
}