package com.project.iosephknecht.barcode_sender_plugin.presentation.features.settings

import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.*
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.settings.SettingsFeatureContract.*

internal class SettingsFeatureFake(
    private val initialState: State,
    private val reducer: Reducer,
    private val producer: Producer,
    private val actionMapper: SettingsFeatureContract.ActionMapper,
    private val newsMapper: SettingsFeatureContract.NewsMapper,
    private val schedulersContainer: SchedulersContainer
) : Feature,
    Store<State, Intent, News> by DefaultStore(
        initialState = initialState,
        reducer = reducer,
        producer = producer,
        actionMapper = actionMapper,
        newsMapper = newsMapper,
        actionBinder = ActionBinder.bindAllSwitchMap(),
        schedulersContainer = schedulersContainer
    )