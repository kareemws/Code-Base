package kw.app.codebase.vm

import android.app.Application
import androidx.collection.ArraySet
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import kw.app.codebase.data.network.RetrofitStore
import kw.app.codebase.data.network.Service
import kw.app.codebase.view.utility.Load
import kw.app.codebase.view.utility.Signal
import kw.app.codebase.view.utility.Signal.Companion.FLAG_CAUSES_NAVIGATION
import kw.app.codebase.view.utility.Signal.Companion.FLAG_IS_LAST_IN_SEQUENCE
import kw.app.codebase.view.utility.Signal.Companion.FLAG_REQUIRES_LOADING
import kw.app.codebase.view.utility.Signal.Companion.FLAG_STOPS_LOADING_AFTER
import kw.app.codebase.view.utility.Signal.Companion.FLAG_STOPS_LOADING_BEFORE
import kw.app.codebase.view.utility.SitIdle
import kw.app.codebase.view.utility.StopLoading
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

    private val _signalsEmitter = MutableLiveData<Signal>()
    val signalsEmitter: LiveData<Signal> = _signalsEmitter

    open fun acknowledgeSignal(signal: Signal) {
        if (signal.flags.contains(FLAG_CAUSES_NAVIGATION)) {
            isWaitingForAcknowledgement = false
            signalsQueue.clear()
            _signalsEmitter.value = SitIdle(signature, ArraySet())
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

    override fun onCleared() {
        super.onCleared()
        RetrofitStore.detachServiceObserver(webServiceObserver)
    }
}