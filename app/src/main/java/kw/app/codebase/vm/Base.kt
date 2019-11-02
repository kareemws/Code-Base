package kw.app.codebase.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import kw.app.codebase.data.network.RetrofitStore
import kw.app.codebase.data.network.Service
import kw.app.codebase.view.utility.Command
import kw.app.codebase.view.utility.Command.SIT_IDLE
import kw.app.codebase.view.utility.Signal
import java.util.*

abstract class Base(application: Application) : AndroidViewModel(application) {

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

    open fun acknowledgeCommand(command: Command) {
        pollNext()
    }

    protected fun enqueueACommand(command: Command) {
        commandsQueue.add(command)
        if (!isWaitingForAcknowledgement) {
            signalsEmitterMLive.postValue(Signal(commandsQueue.poll()!!, this::class.java.name))
            isWaitingForAcknowledgement = true
        }
    }

    private fun pollNext() {
        if (commandsQueue.isEmpty()) {
            isWaitingForAcknowledgement = false
        } else
            signalsEmitterMLive.postValue(Signal(commandsQueue.poll()!!, this::class.java.name))
    }

    private fun emptyCommandsQueue() {
        commandsQueue.clear()
        isWaitingForAcknowledgement = false
    }

    protected fun forceMoveToIdle() {
        emptyCommandsQueue()
        signalsEmitterMLive.value = Signal(SIT_IDLE, this::class.java.name)
    }

    override fun onCleared() {
        super.onCleared()
        RetrofitStore.detachServiceObserver(webServiceObserver)
    }
}