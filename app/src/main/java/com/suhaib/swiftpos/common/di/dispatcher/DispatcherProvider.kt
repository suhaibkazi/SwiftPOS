package com.suhaib.swiftpos.common.di.dispatcher

import kotlinx.coroutines.CoroutineDispatcher

abstract class DispatcherProvider {
    abstract val default: CoroutineDispatcher
    abstract val main: CoroutineDispatcher
    abstract val io: CoroutineDispatcher
}