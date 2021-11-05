package com.project.iosephknecht.barcode_sender_plugin.presentation.multiple_choice.di

import com.intellij.openapi.diagnostic.Logger
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.devices.DevicesFeatureContract
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.devices_selector.DevicesSelectorFeatureContract
import com.project.iosephknecht.barcode_sender_plugin.presentation.multiple_choice.MultipleChoiceDevicesDialog
import com.project.iosephknecht.barcode_sender_plugin.presentation.multiple_choice.component.MultipleChoiceComponentDefault
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides

/**
 * Contract for DI-container [com.project.iosephknecht.presentation.multiple_choice.component.MultipleChoiceComponent].
 *
 * @author IosephKnecht
 */
@MultipleChoiceComponentScope
@Component(modules = [MultipleChoiceComponentModule::class])
internal interface MultipleChoiceComponent {

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance devicesFeature: DevicesFeatureContract.Feature,
            @BindsInstance devicesSelector: DevicesSelectorFeatureContract.Feature
        ): MultipleChoiceComponent
    }

    fun inject(component: MultipleChoiceDevicesDialog)
}

/**
 * Module for DI-container [MultipleChoiceComponent].
 *
 * @author IosephKnecht
 */
@Module
internal class MultipleChoiceComponentModule {

    @MultipleChoiceComponentScope
    @Provides
    fun provideLogger(): Logger {
        return Logger.getInstance(MultipleChoiceComponentDefault::class.java)
    }

    @MultipleChoiceComponentScope
    @Provides
    fun provideComponent(
        devicesFeature: DevicesFeatureContract.Feature,
        devicesSelector: DevicesSelectorFeatureContract.Feature,
        logger: Logger
    ): com.project.iosephknecht.barcode_sender_plugin.presentation.multiple_choice.component.MultipleChoiceComponent {
        return MultipleChoiceComponentDefault(
            devicesFeature = devicesFeature,
            devicesSelector = devicesSelector,
            logger = logger
        )
    }
}