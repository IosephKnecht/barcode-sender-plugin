package com.project.iosephknecht.barcode_sender_plugin.presentation.features.common

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableSource

/**
 * Extension - value for get [Observable].
 *
 * @see [Observable.wrap]
 */
internal inline val <Action : Any> Bootstrapper<*, Action>.actions: Observable<Action>
    get() = Observable.wrap(::subscribe)

/**
 * Contract for external action invokers.
 *
 * @see [ObservableSource]
 *
 * @author IosephKnecht
 */
internal fun interface Bootstrapper<State : Any, Action : Any> : ObservableSource<Action>,
    AwareConfigurableState<State>