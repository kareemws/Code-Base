package kw.app.codebase.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kw.app.codebase.data.network.RetrofitStore
import kw.app.codebase.view.utility.Command
import kw.app.codebase.view.utility.Instruction
import kw.app.codebase.view.utility.Instruction.SIT_IDLE
import java.util.*

abstract class Base(application: Application) : AndroidViewModel(application) {
    protected val webService = RetrofitStore.getServiceObject()

    protected val instructionsQueue = LinkedList<Instruction>()

    protected var isWaitingForAcknowledgement: Boolean = false

    protected val commandEmitter = MutableLiveData<Command>().apply {
        enqueueAnInstruction(SIT_IDLE)
    }

    fun getCommandEmitter(): LiveData<Command> = commandEmitter

    open fun acknowledgeCommand(instruction: Instruction) {
        if (instructionsQueue.isEmpty()) {
            isWaitingForAcknowledgement = false
        } else
            commandEmitter.postValue(Command(instructionsQueue.poll()!!, this::class.java.name))
    }

    protected fun enqueueAnInstruction(instruction: Instruction) {
        instructionsQueue.add(instruction)
        if (!isWaitingForAcknowledgement) {
            commandEmitter.postValue(Command(instructionsQueue.poll()!!, this::class.java.name))
            isWaitingForAcknowledgement = true
        }
    }
}