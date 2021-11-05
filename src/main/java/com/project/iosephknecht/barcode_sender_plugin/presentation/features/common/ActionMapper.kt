package com.project.iosephknecht.barcode_sender_plugin.presentation.features.common

/**
 * Contract for mapper between [Intent] and [Action].
 *
 * @author IosephKnecht
 */
internal fun interface ActionMapper<State : Any, Intent : Any, Action : Any> {

    /**
     * Transform [Intent] to [Action] taking into account current state.
     * If intent not available from current state return null.
     *
     * @param state current state
     * @param intent user wish for execute
     */
    fun map(state: State, intent: Intent): Action?
}