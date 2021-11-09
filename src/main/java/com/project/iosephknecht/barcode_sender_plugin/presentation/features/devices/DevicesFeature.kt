package com.project.iosephknecht.barcode_sender_plugin.presentation.features.devices

import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.*
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.ActionBinder
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.DefaultStore
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.Logger
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.SchedulersContainer
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
    private val logger: Logger,
    private val schedulersContainer: SchedulersContainer
) : Feature,
    Store<State, Intent, News> by DefaultStore(
        initialState = State.UnInitialize,
        reducer = reducer,
        producer = producer,
        actionMapper = { _, intent ->
            when (intent) {
                Intent.Reload -> Action.InvokeReload
            }
        },
        bootstrapper = bootstrapper,
        newsMapper = newsMapper,
        actionBinder = ActionBinder.bindAllSwitchMap(),
        logger = logger,
        schedulersContainer = schedulersContainer
    )