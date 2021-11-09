package com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_generator

import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_generator.BarcodeGeneratorFeatureContract.*
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_generator.BarcodeGeneratorFeatureContract.ActionMapper
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_generator.BarcodeGeneratorFeatureContract.NewsMapper
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.*

/**
 * Default implementation [Feature].
 *
 * @param reducer
 * @param producer
 * @param actionMapper
 * @param newsMapper
 *
 * @author IosephKnecht
 */
internal class BarcodeGeneratorFeatureDefault(
    private val reducer: Reducer,
    private val producer: Producer,
    private val actionMapper: ActionMapper,
    private val newsMapper: NewsMapper,
    private val logger: Logger,
    private val schedulersContainer: SchedulersContainer
) : Feature,
    Store<State, Intent, News> by DefaultStore(
        initialState = State.Empty,
        reducer = reducer,
        producer = producer,
        actionMapper = actionMapper,
        newsMapper = newsMapper,
        actionBinder = ActionBinder.bindAllSwitchMap(),
        logger = logger,
        schedulersContainer = schedulersContainer
    )