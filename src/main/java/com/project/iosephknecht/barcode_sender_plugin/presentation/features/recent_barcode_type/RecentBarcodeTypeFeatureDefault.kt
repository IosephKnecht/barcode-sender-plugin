package com.project.iosephknecht.barcode_sender_plugin.presentation.features.recent_barcode_type

import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.*
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.recent_barcode_type.RecentBarcodeTypeFeatureContract.*
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.recent_barcode_type.RecentBarcodeTypeFeatureContract.ActionMapper
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.recent_barcode_type.RecentBarcodeTypeFeatureContract.Bootstrapper
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.recent_barcode_type.RecentBarcodeTypeFeatureContract.NewsMapper

/**
 * Default implementation [Feature].
 *
 * @author IosephKnecht
 */
internal class RecentBarcodeTypeFeatureDefault(
    private val reducer: Reducer,
    private val producer: Producer,
    private val actionMapper: ActionMapper,
    private val newsMapper: NewsMapper,
    private val bootstrapper: Bootstrapper,
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