package com.project.iosephknecht.barcode_sender_plugin.presentation.settings.di

import com.intellij.openapi.diagnostic.Logger
import com.project.iosephknecht.barcode_sender_plugin.domain.SettingsStorage
import com.project.iosephknecht.barcode_sender_plugin.presentation.app.ApplicationDiComponent
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.settings.SettingsFeatureContract
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.settings.SettingsFeatureFactoryDefault
import com.project.iosephknecht.barcode_sender_plugin.presentation.settings.SettingsDialog
import com.project.iosephknecht.barcode_sender_plugin.presentation.settings.component.SettingsComponent
import com.project.iosephknecht.barcode_sender_plugin.presentation.settings.component.SettingsComponentDefault
import dagger.Component
import dagger.Module
import dagger.Provides

/**
 * Contract for DI-container [SettingsComponent].
 *
 * @author IosephKnecht
 */
@SettingsDiComponentScope
@Component(
    dependencies = [ApplicationDiComponent::class],
    modules = [SettingsDiComponentModule::class]
)
internal interface SettingsDiComponent {

    @Component.Factory
    interface Factory {

        fun create(applicationDiComponent: ApplicationDiComponent): SettingsDiComponent
    }

    fun inject(settingsDialog: SettingsDialog)
}

/**
 * Module for DI-container [SettingsDiComponent].
 *
 * @author IosephKnecht
 */
@Module
internal class SettingsDiComponentModule {

    @SettingsDiComponentScope
    @Provides
    fun provideLogger(): Logger {
        return Logger.getInstance(SettingsComponentDefault::class.java)
    }

    @SettingsDiComponentScope
    @Provides
    fun provideSettingsFeature(settingsStorage: SettingsStorage): SettingsFeatureContract.Feature {
        return SettingsFeatureFactoryDefault(settingsStorage).create()
    }

    @SettingsDiComponentScope
    @Provides
    fun provideSettingsComponent(
        settingsFeature: SettingsFeatureContract.Feature,
        logger: Logger
    ): SettingsComponent {
        return SettingsComponentDefault(settingsFeature, logger)
    }
}