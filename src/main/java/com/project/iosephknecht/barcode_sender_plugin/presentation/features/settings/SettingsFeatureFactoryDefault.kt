package com.project.iosephknecht.barcode_sender_plugin.presentation.features.settings

import com.project.iosephknecht.barcode_sender_plugin.domain.SettingsStorage
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.SchedulersContainer
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.Swing
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.settings.SettingsFeatureContract.*

/**
 * Factory for create default implementation [SettingsFeatureContract.Feature].
 *
 * @param settingsStorage
 *
 * @author IosephKnecht
 */
internal class SettingsFeatureFactoryDefault(
    private val settingsStorage: SettingsStorage,
    private val schedulersContainer: SchedulersContainer = SchedulersContainer.Swing
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
        return SettingsFeatureDefault(
            reducer = reducer,
            producer = producer,
            actionMapper = actionMapper,
            newsMapper = newsMapper,
            schedulersContainer = schedulersContainer
        )
    }
}