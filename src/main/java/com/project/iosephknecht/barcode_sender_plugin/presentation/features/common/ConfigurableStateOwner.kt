package com.project.iosephknecht.barcode_sender_plugin.presentation.features.common

import java.util.concurrent.atomic.AtomicReference

internal fun <State : Any> AwareConfigurableState<State>.init(stateSupplier: () -> State) =
    configure { init(stateSupplier) }

internal fun <State : Any> AwareConfigurableState<State>.dispose() = configure { dispose() }

@Suppress("UNCHECKED_CAST")
private inline fun <State : Any> AwareConfigurableState<State>.configure(block: ConfigurableStateOwner<State>.() -> Unit) {
    if (this !is ConfigurableStateOwner<*>) return
    this as ConfigurableStateOwner<State>
    block.invoke(this)
}

/**
 * A marker interface for classes that can implement [ConfigurableStateOwner].
 *
 * @author IosephKnecht
 */
internal interface AwareConfigurableState<State : Any>

/**
 * State container contract.
 *
 * Example for usage:
 * ```
 *  class ConcreteEffectProducer: EffectProducer<ConcreteState, ConcreteAction, ConcreteEffect>,
 *      AwareConfigurableState<ConcreteState>,
 *      ConfigurableStateOwner<ConcreteState> {
 *
 *      val state: ConcreteState
 *          get() = // get state from reference
 *
 *      fun init(stateSupplier: () -> ConcreteState) {
 *          // remember reference of state supplier
 *      }
 *
 *      fun produce(action: ConcreteAction): Observable<ConcreteEffect> {
 *          // some code
 *      }
 *
 *      fun dispose() {
 *          // clear reference of state supplier
 *      }
 *  }
 * ```
 *
 * @author IosephKnecht
 */
internal interface ConfigurableStateOwner<State : Any> {

    /**
     * State.
     */
    val state: State

    /**
     * Initialize state owner.
     *
     * @param stateSupplier supplier for state.
     */
    fun init(stateSupplier: () -> State)

    /**
     * Unsubscribe state owner.
     */
    fun dispose()
}

/**
 * Default implementation for [ConfigurableStateOwner].
 *
 * @author IosephKnecht
 */
internal class DefaultConfigurableStateOwner<State : Any> : ConfigurableStateOwner<State> {

    private val ref = AtomicReference<() -> State?>()

    override val state: State
        get() = requireNotNull(ref.get()).invoke()!!

    override fun init(stateSupplier: () -> State) {
        ref.compareAndSet(null, stateSupplier).also { require(it) { "value already initialized" } }
    }

    override fun dispose() {
        if (ref.get() === DISPOSED) return
        ref.set(DISPOSED as () -> State?)
    }

    private companion object {
        private val DISPOSED: () -> Any? = { null }
    }
}