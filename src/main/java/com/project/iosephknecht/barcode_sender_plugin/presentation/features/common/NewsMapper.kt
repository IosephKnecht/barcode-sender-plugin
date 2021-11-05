package com.project.iosephknecht.barcode_sender_plugin.presentation.features.common

/**
 * Mapper for producing [News].
 *
 * @author IosephKnecht
 */
internal fun interface NewsMapper<State : Any, Effect : Any, News : Any> {

    /**
     * Transform previous state and effect to [News].
     *
     * @param state previous state.
     * @param effect effect for reducing.
     */
    fun map(state: State, effect: Effect): News?
}