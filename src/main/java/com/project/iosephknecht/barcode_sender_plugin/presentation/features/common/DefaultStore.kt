package com.project.iosephknecht.barcode_sender_plugin.presentation.features.common

import hu.akarnokd.rxjava3.swing.SwingSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject

/**
 * Default implementation for [Store].
 * Attention is final class not supported inheritance.
 *
 * Example for usage:
 * ```
 *  class ConcreteFeature(
 *      initialState: ConcreteState,
 *      reducer: StateReducer<ConcreteState, ConcreteEffect>,
 *      producer: EffectProducer<ConcreteState, ConcreteAction, ConcreteEffect>,
 *      actionMapper: ActionMapper<ConcreteState, ConcreteIntent, ConcreteAction>
 *  ) : Store<ConcreteState, ConcreteIntent, ConcreteNews> by DefaultStore(
 *      initialState = initialState,
 *      reducer = reducer,
 *      producer = producer,
 *      actionMapper = actionMapper
 *  )
 * ```
 *
 * @param initialState initial state.
 * @param reducer state reducer.
 * @param producer effect producer.
 * @param actionMapper action mapper.
 * @param bootstrapper bootstrapper (optional).
 * @param newsMapper news mapper (optional).
 * @param actionBinder action binder (optional).
 *
 * @author IosephKnecht
 */
internal class DefaultStore<State : Any, Intent : Any, Action : Any, Effect : Any, News : Any>(
    initialState: State,
    reducer: StateReducer<State, Effect>,
    private val producer: EffectProducer<State, Action, Effect>,
    private val actionMapper: ActionMapper<State, Intent, Action>,
    private val bootstrapper: Bootstrapper<State, Action>? = null,
    newsMapper: NewsMapper<State, Effect, News>? = null,
    actionBinder: ActionBinder<Action>? = null,
    logger: Logger = Logger.printStackTraceLogger()
) : Store<State, Intent, News> {

    private val actionSubject = PublishSubject.create<Action>()
    private val stateSubject = BehaviorSubject.createDefault(initialState)
    private val newsSubject = PublishSubject.create<News>()

    private val scope = DefaultActionBinderScope<State, Action, Effect>(
        actions = actionSubject,
        effectProducer = producer
    )
    private val disposable = CompositeDisposable()

    override val state: State
        get() = stateSubject.value!!

    init {
        producer.init(::state)
        bootstrapper?.init(::state)

        val effects: Observable<Effect> = when (actionBinder) {
            null -> actionSubject.switchMap(producer::produce)
            else -> actionBinder.apply { scope.bind() }
                .run { scope.get() }
                .also { list -> require(list.isNotEmpty()) { "effects must not be empty" } }
                .let { Observable.merge(it) }
        }

        effects
            .observeOn(SwingSchedulers.edt())
            .subscribe(
                { effect ->
                    val previousState = stateSubject.value!!
                    val newState = reducer.reduce(previousState, effect)
                    val news = newsMapper?.map(previousState, effect)

                    stateSubject.onNext(newState)
                    news?.let(newsSubject::onNext)
                },
                logger::error
            )
            .let(disposable::add)

        bootstrapper
            ?.actions
            ?.observeOn(SwingSchedulers.edt())
            ?.subscribe(
                actionSubject::onNext,
                logger::error
            )
            ?.let(disposable::add)
    }

    override fun accept(intent: Intent) {
        actionMapper.map(stateSubject.value!!, intent)?.let(actionSubject::onNext)
    }

    override fun observeStates(observer: Observer<in State>) {
        stateSubject.subscribe(observer)
    }

    override fun observeNews(observer: Observer<in News>) {
        newsSubject.subscribe(observer)
    }

    override fun dispose() {
        actionSubject.onComplete()
        stateSubject.onComplete()
        newsSubject.onComplete()
        disposable.clear()
        producer.dispose()
        bootstrapper?.dispose()
    }

    override fun isDisposed() = disposable.isDisposed

    private class DefaultActionBinderScope<State : Any, Action : Any, Effect : Any>(
        private val actions: Observable<Action>,
        private val effectProducer: EffectProducer<State, Action, Effect>
    ) : ActionBinderScope<Action> {

        private val effects = linkedMapOf<Class<Action>, Observable<Effect>>()

        fun get(): List<Observable<Effect>> = effects.values.toList()

        @Suppress("UNCHECKED_CAST")
        override fun <T : Action> switchMap(clazz: Class<T>) {
            effects[clazz as Class<Action>] = actions.ofType(clazz).switchMap(effectProducer::produce)
        }

        @Suppress("UNCHECKED_CAST")
        override fun <T : Action> flatMap(clazz: Class<T>) {
            effects[clazz as Class<Action>] = actions.ofType(clazz).flatMap(effectProducer::produce)
        }

    }
}