package com.project.iosephknecht.barcode_sender_plugin.presentation.features.common

/**
 * Contract for State Reducer.
 *
 * @author IosephKnecht
 */
internal fun interface StateReducer<State : Any, Effect : Any> {

    /**
     * Transform current state to new state based on effect.
     *
     * @param state current state.
     * @param effect effect for transition.
     *
     * @return new state
     */
    fun reduce(state: State, effect: Effect): State
}