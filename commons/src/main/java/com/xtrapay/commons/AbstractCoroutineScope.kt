package com.xtrapay.commons

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

abstract class AbstractCoroutineScope(private val dispatcher: CoroutineDispatcher = Dispatchers.IO) : CoroutineScope {
    private val job = Job()


    override val coroutineContext: CoroutineContext
        get() = dispatcher + job


    open fun destroy() = job.cancel()
}


fun <T> CoroutineScope.runOnMain(block: () -> T) = launch(Dispatchers.Main) {
    block()
}

fun <T> runOnMain(block: () -> T) = GlobalScope.launch(Dispatchers.Main) {
    block()
}

fun <T> runOnBackground(block: () -> T) = GlobalScope.launch(Dispatchers.IO) {
    block()
}