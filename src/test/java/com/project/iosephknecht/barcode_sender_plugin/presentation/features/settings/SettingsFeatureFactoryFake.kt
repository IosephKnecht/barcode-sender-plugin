package com.project.iosephknecht.barcode_sender_plugin.presentation.features.settings

import com.project.iosephknecht.barcode_sender_plugin.domain.SettingsStorage
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.SchedulersContainer
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.settings.SettingsFeatureContract.*

internal class SettingsFeatureFactoryFake(
    private val initialState: State,
    private val settingsStorage: SettingsStorage,
    private val schedulersContainer: SchedulersContainer
) {

    private val reducer by lazy {
        SettingsReducerDefault()
    }

    private val producer by lazy {
        SettingsProducerDefault(settingsStorage)
    }

    private val actionMapper by lazy {
        SettingsActionMapperDefault()
    }

    private val newsMapper by lazy {
        SettingsNewsMapperDefault()
    }

    fun create(): Feature {
        return SettingsFeatureFake(
            initialState = initialState,
            reducer = reducer,
            producer = producer,
            actionMapper = actionMapper,
            newsMapper = newsMapper,
            schedulersContainer = schedulersContainer
        )
    }
}