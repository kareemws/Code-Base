package kw.app.codebase.view.utility

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import kw.app.codebase.vm.App
import kw.app.codebase.vm.Base
import java.util.*

abstract class BaseFragment : Fragment() {

    protected val avm: App by activityViewModels()

    private val signalsQueue = LinkedList<Signal>()

    private val internalSignalsEmitterMLive = MutableLiveData<Signal>()
    protected val internalSignalsEmitter: LiveData<Signal> = internalSignalsEmitterMLive

    private var isProcessing: Boolean = false


    protected val externalSignalsObserver = Observer<Signal> { signal ->
        signalsQueue.add(signal)
        if (!isProcessing) {
            isProcessing = true
            resume()
        }
    }

    protected fun resume() {
        if (signalsQueue.isEmpty())
            isProcessing = false
        else
            internalSignalsEmitterMLive.postValue(signalsQueue.poll())
    }

    protected fun setupSignalsObservers(
        signalsObserver: Observer<Signal>,
        vararg viewModels: Base
    ) {
        viewModels.forEach { vm ->
            vm.signalsEmitter.observe(viewLifecycleOwner, externalSignalsObserver)
        }
        internalSignalsEmitter.observe(viewLifecycleOwner, signalsObserver)
    }
}