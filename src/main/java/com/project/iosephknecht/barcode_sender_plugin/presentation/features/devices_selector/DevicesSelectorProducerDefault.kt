package com.project.iosephknecht.barcode_sender_plugin.presentation.features.devices_selector

import io.reactivex.rxjava3.core.Observable
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.devices_selector.DevicesSelectorFeatureContract.*

/**
 * Implementation effect producer for devices selector feature.
 *
 * @author IosephKnecht
 */
internal class DevicesSelectorProducerDefault : Producer {

    override fun produce(action: Intent): Observable<Effect> {
        return when (action) {
            is Intent.ResetSelection -> minus(action)
            is Intent.SetSelection -> plus(action)
        }
    }

    private fun minus(intent: Intent.ResetSelection): Observable<Effect> {
        return Observable.just(Effect.ResetSelection(intent.devices.toSet()))
    }

    private fun plus(intent: Intent.SetSelection): Observable<Effect> {
        return Observable.just(Effect.AddSelection(intent.devices.toSet()))
    }
}