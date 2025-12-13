package com.xtrapay.commons

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.xtrapay.poslib.entities.HostConfig
import com.xtrapay.poslib.entities.TransactionRequestData
import com.xtrapay.poslib.entities.TransactionResponse
import com.xtrapay.poslib.processors.TransactionProcessor
import io.reactivex.*

abstract class EmvProcessor(val activity: AppCompatActivity,
                            val hostConfig: HostConfig
): LifecycleObserver, AbstractCoroutineScope() {

    private var isAlive =  false

    private lateinit var resultEmitter: ObservableEmitter<TransactionResponse>
    private lateinit var stateEmitter: FlowableEmitter<ProcessState>

    init {
        activity.lifecycle.addObserver(this)
    }

    val processor by lazy {
        TransactionProcessor(hostConfig)
    }


    val onResponse: Observable<TransactionResponse> by lazy {
        Observable.create<TransactionResponse> {
            resultEmitter = it
        }
    }

    val onStateChanged: Flowable<ProcessState> by lazy {
        Flowable.create<ProcessState> ({
            stateEmitter = it
        }, BackpressureStrategy.LATEST)
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun onResume() {
        isAlive = true
    }

    fun setError(e: Throwable) {
        if (isAlive) {
            resultEmitter.tryOnError(e)
        }
    }

    fun setResponse(response: TransactionResponse) {
        if (isAlive) {
            resultEmitter.onNext(response)
        }
    }

    fun setState(state: ProcessState) {
        if (isAlive) {
            stateEmitter.onNext(state)
        }
    }

    override fun destroy() {
        super.destroy()
        onDestroy()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun onDestroy() {
        isAlive = false
        activity.lifecycle.removeObserver(this)

        stateEmitter.onComplete()
        resultEmitter.onComplete()
    }


    abstract fun startTransaction(requestData: TransactionRequestData, entryMode: String, )

    abstract fun detectCard(reader: Reader = Reader.CT_CTLS)

    abstract fun abortTransaction()

}



//sealed class ProcessState2(
//    override val state: States,
//    override val message: String): ProcessState(state, message) {
//    data class PreProcessing(val preProcessing: TransactionResponse): ProcessState2(state = ProcessState.States.PREPROCESSING, "")
//}


data class ProcessState (val state: States,
                         val message: String,
                         val preProcessing: TransactionResponse? = null
) {
    enum class States {
        CARD_ENTRY, READ_CARD, PROCESSING, PREPROCESSING, MESSAGE, CARD_FOUND
    }
}
