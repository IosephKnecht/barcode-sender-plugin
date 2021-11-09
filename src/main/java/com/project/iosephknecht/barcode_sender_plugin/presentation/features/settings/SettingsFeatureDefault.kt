package com.project.iosephknecht.barcode_sender_plugin.presentation.features.settings

import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.*
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.ActionBinder
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.DefaultStore
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.SchedulersContainer
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.Store
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.bindAllSwitchMap
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.settings.SettingsFeatureContract.*
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.settings.SettingsFeatureContract.ActionMapper
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.settings.SettingsFeatureContract.NewsMapper

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
internal class SettingsFeatureDefault(
    private val reducer: Reducer,
    private val producer: Producer,
    private val actionMapper: ActionMapper,
    private val newsMapper: NewsMapper,
    private val schedulersContainer: SchedulersContainer
) : Feature,
    Store<State, Intent, News> by DefaultStore(
        initialState = State.NotInitialize,
        reducer = reducer,
        producer = producer,
        actionMapper = actionMapper,
        newsMapper = newsMapper,
        actionBinder = ActionBinder.bindAllSwitchMap(),
        schedulersContainer = schedulersContainer
    )