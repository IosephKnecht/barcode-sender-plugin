package com.project.iosephknecht.barcode_sender_plugin.presentation.features.devices

import com.project.iosephknecht.barcode_sender_plugin.presentation.features.devices.DevicesFeatureContract.*

/**
 * Implementation for [DevicesFeatureContract.NewsMapper].
 *
 * @author IosephKnecht
 */
internal class DevicesNewsMapper : NewsMapper {

    override fun map(
        state: State,
        effect: Effect
    ): News? {
        return when (state) {
            State.ProcessInitialize -> when (effect) {
                is Effect.SuccessLoadDevices -> News.DeviceListChanged
                else -> null
            }
            is State.ProcessLoadDevices -> when (effect) {
                is Effect.SuccessLoadDevices -> News.DeviceListChanged
                else -> null
            }
            else -> null
        }
    }
}