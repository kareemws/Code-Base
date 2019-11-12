package kw.base.module.signal.view

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import kw.base.module.signal.model.Signal
import kw.base.module.signal.vm.Base
import java.util.*

abstract class BaseFragment : Fragment() {

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
        internalSignalsEmitter.observe(viewLifecycleOwner, signalsObserver)
        viewModels.forEach { vm ->
            vm.signalsEmitter.observe(viewLifecycleOwner, externalSignalsObserver)
        }
    }
}