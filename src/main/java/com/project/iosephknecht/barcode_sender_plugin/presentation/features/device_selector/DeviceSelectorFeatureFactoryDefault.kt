package com.project.iosephknecht.barcode_sender_plugin.presentation.features.device_selector

import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.Logger
import com.project.iosephknecht.barcode_sender_plugin.domain.SettingsStorage
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.SchedulersContainer
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.Swing
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.devices.DevicesFeatureContract
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.intellijIdeaLogger

/**
 * Factory for provide default implementation [DeviceSelectorFeatureContract.Feature].
 *
 * @param devicesFeature feature of devices.
 * @param settingsStorage settings storage.
 *
 * @author IosephKnecht
 */
internal class DeviceSelectorFeatureFactoryDefault(
    private val devicesFeature: DevicesFeatureContract.Feature,
    private val settingsStorage: SettingsStorage,
    private val schedulersContainer: SchedulersContainer = SchedulersContainer.Swing
) {

    private val reducer by lazy {
        DeviceSelectorFeatureReducerDefault()
    }

    private val producer by lazy {
        DeviceSelectorFeatureProducerDefault(settingsStorage, schedulersContainer)
    }

    private val bootstrapper by lazy {
        DeviceSelectorFeatureBootstrapperDefault(devicesFeature, schedulersContainer)
    }

    private val actionMapper by lazy {
        DeviceSelectorFeatureActionMapperDefault()
    }

    private val newsMapper by lazy {
        DeviceSelectorNewsMapperDefault()
    }

    private val logger by lazy {
        Logger.intellijIdeaLogger(DeviceSelectorFeatureDefault::class)
    }

    fun create(): DeviceSelectorFeatureContract.Feature {
        return DeviceSelectorFeatureDefault(
            reducer = reducer,
            producer = producer,
            bootstrapper = bootstrapper,
            actionMapper = actionMapper,
            newsMapper = newsMapper,
            logger = logger,
            schedulersContainer = schedulersContainer
        )
    }
}