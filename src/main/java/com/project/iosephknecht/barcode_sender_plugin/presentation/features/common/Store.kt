package com.project.iosephknecht.barcode_sender_plugin.presentation.features.common

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Consumer

internal inline val <State : Any> Store<State, *, *>.states: Observable<State>
    get() = Observable.wrap(::observeStates)

internal inline val <News : Any> Store<*, *, News>.news: Observable<News>
    get() = Observable.wrap(::observeNews)

/**
 * Contract for Store.
 * Store is a consumer [Intent].
 * Store is an observable [State] (@see [Store.states]).
 * Store is an observable [News] (@see [Store.news]).
 *
 * @author IosephKnecht
 */
internal interface Store<State : Any, Intent : Any, News : Any> : Consumer<Intent>, Disposable {

    /**
     * Current state store.
     */
    val state: State

    /**
     * Send intent for execute.
     *
     * @param intent user wish.
     */
    override fun accept(intent: Intent)

    /**
     * Subscribe on changes state fields.
     *
     * @param observer
     *
     * @see [Observer], [io.reactivex.rxjava3.core.ObservableSource.subscribe]
     */
    fun observeStates(observer: Observer<in State>)

    /**
     * Subscribe on changes news.
     *
     * @param observer
     *
     * @see [Observer], [io.reactivex.rxjava3.core.ObservableSource.subscribe]
     */
    fun observeNews(observer: Observer<in News>)
}