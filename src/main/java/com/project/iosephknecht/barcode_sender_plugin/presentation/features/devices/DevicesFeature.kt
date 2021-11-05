package com.project.iosephknecht.barcode_sender_plugin.presentation.features.devices

import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.ActionBinder
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.DefaultStore
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.Logger
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.Store
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.bindAllSwitchMap
import  com.project.iosephknecht.barcode_sender_plugin.presentation.features.devices.DevicesFeatureContract.*
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.devices.DevicesFeatureContract.Bootstrapper
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.devices.DevicesFeatureContract.NewsMapper

/**
 * Implementation devices feature.
 *
 * @see [DevicesFeatureContract.Feature]
 *
 * @param bootstrapper
 * @param reducer
 * @param producer
 * @param newsMapper
 *
 * @author IosephKnecht
 */
internal class DevicesFeature(
    private val bootstrapper: Bootstrapper,
    private val producer: Producer,
    private val reducer: Reducer,
    private val newsMapper: NewsMapper,
    private val logger: Logger
) : Feature,
    Store<State, Intent, News> by DefaultStore(
        initialState = State.UnInitialize,
        reducer = reducer,
        producer = producer,
        bootstrapper = bootstrapper,
        newsMapper = newsMapper,
        actionMapper = { _, intent ->
            when (intent) {
                Intent.Reload -> Action.InvokeReload
            }
        },
        actionBinder = ActionBinder.bindAllSwitchMap(),
        logger = logger
    )