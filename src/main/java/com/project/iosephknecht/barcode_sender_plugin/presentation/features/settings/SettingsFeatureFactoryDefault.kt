package com.project.iosephknecht.barcode_sender_plugin.presentation.features.settings

import com.project.iosephknecht.barcode_sender_plugin.domain.SettingsStorage
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.settings.SettingsFeatureContract.*

/**
 * Factory for create default implementation [SettingsFeatureContract.Feature].
 *
 * @param settingsStorage
 *
 * @author IosephKnecht
 */
internal class SettingsFeatureFactoryDefault(
    private val settingsStorage: SettingsStorage
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
            newsMapper = newsMapper
        )
    }
}