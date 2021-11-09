package com.project.iosephknecht.barcode_sender_plugin.presentation.features.devices_selector

import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.Logger
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.SchedulersContainer
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.Swing
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.device_selector.DeviceSelectorFeatureContract
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.devices.DevicesFeatureContract
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.intellijIdeaLogger

/**
 * Factory for provide default implementation [DevicesSelectorFeatureContract.Feature].
 *
 * @author IosephKnecht
 */
internal class DevicesSelectorFeatureFactoryDefault(
    private val devicesFeature: DevicesFeatureContract.Feature,
    private val deviceSelector: DeviceSelectorFeatureContract.Feature,
    private val schedulersContainer: SchedulersContainer = SchedulersContainer.Swing
) {

    private val reducer by lazy {
        DevicesSelectorReducerDefault()
    }

    private val producer by lazy {
        DevicesSelectorProducerDefault()
    }

    private val bootstrapper by lazy {
        DevicesSelectorBootstrapperDefault(devicesFeature, deviceSelector, schedulersContainer)
    }

    private val logger by lazy {
        Logger.intellijIdeaLogger(DevicesSelectorFeatureDefault::class)
    }

    fun create(): DevicesSelectorFeatureContract.Feature {
        return DevicesSelectorFeatureDefault(
            reducer = reducer,
            producer = producer,
            bootstrapper = bootstrapper,
            logger = logger,
            schedulersContainer = schedulersContainer
        )
    }
}