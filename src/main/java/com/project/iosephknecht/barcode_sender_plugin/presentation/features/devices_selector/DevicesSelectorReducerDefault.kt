package com.project.iosephknecht.barcode_sender_plugin.presentation.features.devices_selector

import com.project.iosephknecht.barcode_sender_plugin.presentation.features.devices_selector.DevicesSelectorFeatureContract.*

/**
 * Implementation state reducer for devices selector feature.
 *
 * @author IosephKnecht
 */
internal class DevicesSelectorReducerDefault : Reducer {

    override fun reduce(
        state: State,
        effect: Effect
    ): State {
        return when (effect) {
            is Effect.ResetSelection -> State(devices = state.devices - effect.devices)
            is Effect.AddSelection -> State(devices = state.devices + effect.devices)
        }
    }
}