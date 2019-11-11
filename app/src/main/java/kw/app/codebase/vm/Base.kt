package kw.app.codebase.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import kw.app.codebase.data.network.RetrofitStore
import kw.app.codebase.data.network.Service
import kw.app.codebase.view.utility.Command
import kw.app.codebase.view.utility.Command.*
import kw.app.codebase.view.utility.Signal
import java.util.*

abstract class Base(application: Application) : AndroidViewModel(application) {

    val signature: String = this::class.java.name

    protected var webService = RetrofitStore.getServiceObject()
    private val webServiceObserver = Observer<Service> { service ->
        webService = service
    }

    init {
        RetrofitStore.attachServiceObserver(webServiceObserver)
    }

    private val commandsQueue = LinkedList<Command>()

    private var isWaitingForAcknowledgement: Boolean = false

    private val signalsEmitterMLive = MutableLiveData<Signal>()

    val signalsEmitter: LiveData<Signal> = signalsEmitterMLive

    open fun acknowledgeSignal(signal: Signal) {
        if (signal.signature == signature)
            pollNext()
    }

    protected fun enqueueCommand(
        command: Command,
        isLastInSequence: Boolean = false,
        requiresLoading: Boolean = false,
        stopsLoadingBefore: Boolean = false,
        stopsLoadingAfter: Boolean = false,
        causesNavigation: Boolean = false
    ) {
        if (requiresLoading)
            commandsQueue.add(LOAD)
        if (stopsLoadingBefore && !requiresLoading)
            commandsQueue.add(STOP_LOADING)

        commandsQueue.add(command)

        if (stopsLoadingAfter && !stopsLoadingBefore)
            commandsQueue.add(STOP_LOADING)
        if (isLastInSequence || causesNavigation)
            commandsQueue.add(SIT_IDLE)
        if (!isWaitingForAcknowledgement) {
            signalsEmitterMLive.postValue(Signal(commandsQueue.poll()!!, signature))
            isWaitingForAcknowledgement = true
        }
    }

    private fun pollNext() {
        if (commandsQueue.isEmpty()) {
            isWaitingForAcknowledgement = false
        } else
            signalsEmitterMLive.postValue(Signal(commandsQueue.poll()!!, signature))
    }

    override fun onCleared() {
        super.onCleared()
        RetrofitStore.detachServiceObserver(webServiceObserver)
    }
}