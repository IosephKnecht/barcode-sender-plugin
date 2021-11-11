package com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_generator_multiple

import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_generator_multiple.BarcodeGeneratorMultipleFeatureContract.*
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_generator_multiple.BarcodeGeneratorMultipleFeatureContract.ActionMapper
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_generator_multiple.BarcodeGeneratorMultipleFeatureContract.NewsMapper
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.*
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.ActionBinder
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.DefaultStore
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.Logger
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.SchedulersContainer
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.Store
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.bindAllSwitchMap

/**
 * Default implementation [Feature] for generate multiple barcode.
 *
 * @param initialState
 * @param reducer
 * @param producer
 * @param actionMapper
 * @param newsMapper
 *
 * @author IosephKnecht
 */
internal class BarcodeGeneratorMultipleFeatureDefault(
    private val initialState: State,
    private val reducer: Reducer,
    private val producer: Producer,
    private val actionMapper: ActionMapper,
    private val newsMapper: NewsMapper,
    private val logger: Logger,
    private val schedulersContainer: SchedulersContainer
) : Feature,
    Store<State, Intent, News> by DefaultStore(
        initialState = initialState,
        reducer = reducer,
        producer = producer,
        actionMapper = actionMapper,
        newsMapper = newsMapper,
        actionBinder = ActionBinder.bindAllSwitchMap(),
        logger = logger,
        schedulersContainer = schedulersContainer
    )