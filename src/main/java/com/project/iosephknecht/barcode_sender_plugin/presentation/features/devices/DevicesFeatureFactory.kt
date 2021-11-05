package com.project.iosephknecht.barcode_sender_plugin.presentation.features.devices

import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.Logger
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.intellijIdeaLogger

internal class DevicesFeatureFactory {

    private val bootstrapper by lazy {
        DevicesBootstrapper()
    }

    private val effectProducer by lazy {
        DevicesEffectProducer()
    }

    private val reducer by lazy {
        DevicesStateReducer()
    }

    private val newsMapper by lazy {
        DevicesNewsMapper()
    }

    private val logger by lazy {
        Logger.intellijIdeaLogger(DevicesFeature::class)
    }

    fun create(): DevicesFeatureContract.Feature {
        return DevicesFeature(
            bootstrapper = bootstrapper,
            producer = effectProducer,
            reducer = reducer,
            newsMapper = newsMapper,
            logger = logger
        )
    }
}