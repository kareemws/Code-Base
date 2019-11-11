package kw.app.codebase.vm

import android.app.Application
import androidx.collection.ArraySet
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import kw.app.codebase.data.network.RetrofitStore
import kw.app.codebase.data.network.Service
import kw.app.codebase.view.utility.Command
import kw.app.codebase.view.utility.Command.*
import kw.app.codebase.view.utility.Signal
import kw.app.codebase.view.utility.Signal.Companion.FLAG_CAUSES_NAVIGATION
import kw.app.codebase.view.utility.Signal.Companion.FLAG_IS_LAST_IN_SEQUENCE
import kw.app.codebase.view.utility.Signal.Companion.FLAG_REQUIRES_LOADING
import kw.app.codebase.view.utility.Signal.Companion.FLAG_STOPS_LOADING_AFTER
import kw.app.codebase.view.utility.Signal.Companion.FLAG_STOPS_LOADING_BEFORE
import java.util.*

abstract class Base(application: Application) : AndroidViewModel(application) {

    protected var webService = RetrofitStore.getServiceObject()
    private val webServiceObserver = Observer<Service> { service ->
        webService = service
    }

    init {
        RetrofitStore.attachServiceObserver(webServiceObserver)
    }


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

    protected fun enqueueCommand(command: Command, vararg flags: Int) {
        val flagsSet = flags.toCollection(ArraySet())

        if (flagsSet.contains(FLAG_REQUIRES_LOADING))
            signalsQueue.add(Signal(LOAD, signature, flagsSet))
        else if (flagsSet.contains(FLAG_STOPS_LOADING_BEFORE))
            signalsQueue.add(Signal(STOP_LOADING, signature, flagsSet))

        signalsQueue.add(Signal(command, signature, flagsSet))

        if (flagsSet.contains(FLAG_STOPS_LOADING_AFTER)
            && !flagsSet.contains(FLAG_STOPS_LOADING_BEFORE)
        )
            signalsQueue.add(Signal(STOP_LOADING, signature, flagsSet))

        if (flagsSet.contains(FLAG_IS_LAST_IN_SEQUENCE))
            signalsQueue.add(Signal(SIT_IDLE, signature, flagsSet))

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

    override fun onCleared() {
        super.onCleared()
        RetrofitStore.detachServiceObserver(webServiceObserver)
    }
}