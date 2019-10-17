package kw.app.codebase.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kw.app.codebase.data.network.RetrofitStore
import kw.app.codebase.view.utility.Command
import kw.app.codebase.view.utility.Command.*

abstract class Base(application: Application): AndroidViewModel(application) {
    protected val webService = RetrofitStore.getServiceObject()

    protected val commandEmitter = MutableLiveData<Command>().apply {
        postValue(SET_IDLE)
    }

    fun getCommandEmitter(): LiveData<Command> = commandEmitter

    open fun acknowledgeReceivingCommand(){

    }
}