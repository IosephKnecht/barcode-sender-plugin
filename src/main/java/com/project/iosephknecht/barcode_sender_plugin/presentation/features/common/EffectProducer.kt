package com.project.iosephknecht.barcode_sender_plugin.presentation.features.common

import io.reactivex.rxjava3.core.Observable

/**
 * Contract for effect producer.
 * Transform incoming action to observable effects.
 *
 * @author IosephKnecht
 */
internal fun interface EffectProducer<State : Any, Action : Any, Effect : Any> : AwareConfigurableState<State> {

    /**
     * Transforming incoming action to Observable<Effect>.
     *
     * @param action action for execute.
     */
    fun produce(action: Action): Observable<Effect>
}