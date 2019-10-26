package kw.app.codebase.view.utility

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import kw.app.codebase.vm.App
import java.util.*

open class BaseFragment : Fragment() {
    protected val avm: App by activityViewModels()

    protected val commandQueue = LinkedList<Command>()

    protected val internalCommandEmitter = MutableLiveData<Command>()

    protected var isProcessing: Boolean = false

    protected val externalCommandObserver = Observer<Command> { command ->
        commandQueue.add(command)
        if (!isProcessing) {
            isProcessing = true
            internalCommandEmitter.postValue(commandQueue.poll())
        }
    }

    protected fun resume() {
        if (commandQueue.isEmpty())
            isProcessing = false
        else
            internalCommandEmitter.postValue(commandQueue.poll())
    }
}