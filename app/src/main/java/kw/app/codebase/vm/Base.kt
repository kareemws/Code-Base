package kw.app.codebase.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kw.app.codebase.data.network.RetrofitClientConstructor
import kw.app.codebase.view.utility.Command
import kw.app.codebase.view.utility.Command.*

abstract class Base(application: Application): AndroidViewModel(application) {
    protected val webService = RetrofitClientConstructor.getServiceObject()

    protected val commandEmitter = MutableLiveData<Command>().apply {
        postValue(SET_IDLE)
    }

    fun getCommandEmitter(): LiveData<Command> = commandEmitter

    open fun acknowledgeReceivingCommand(){

    }
}