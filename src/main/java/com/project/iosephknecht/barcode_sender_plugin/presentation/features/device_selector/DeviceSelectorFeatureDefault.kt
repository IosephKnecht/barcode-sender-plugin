package com.project.iosephknecht.barcode_sender_plugin.presentation.features.device_selector

import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.*
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.ActionBinder
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.DefaultStore
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.Logger
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.SchedulersContainer
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.Store
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.bindAllSwitchMap
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.device_selector.DeviceSelectorFeatureContract.*
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.device_selector.DeviceSelectorFeatureContract.ActionMapper
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.device_selector.DeviceSelectorFeatureContract.Bootstrapper
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.device_selector.DeviceSelectorFeatureContract.NewsMapper

/**
 * Implementation for device selector feature.
 *
 * @param reducer
 * @param producer
 * @param newsMapper
 *
 * @author IosephKnecht
 */
internal class DeviceSelectorFeatureDefault(
    private val reducer: Reducer,
    private val producer: Producer,
    private val bootstrapper: Bootstrapper,
    private val actionMapper: ActionMapper,
    private val newsMapper: NewsMapper,
    private val logger: Logger,
    private val schedulersContainer: SchedulersContainer
) : Feature,
    Store<State, Intent, News> by DefaultStore(
        initialState = State.NotInitialized,
        reducer = reducer,
        producer = producer,
        actionMapper = actionMapper,
        bootstrapper = bootstrapper,
        newsMapper = newsMapper,
        actionBinder = ActionBinder.bindAllSwitchMap(),
        logger = logger,
        schedulersContainer = schedulersContainer
    )