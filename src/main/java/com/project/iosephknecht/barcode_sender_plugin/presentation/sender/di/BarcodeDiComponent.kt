package com.project.iosephknecht.barcode_sender_plugin.presentation.sender.di

import com.intellij.openapi.diagnostic.Logger
import com.project.iosephknecht.barcode_sender_plugin.domain.BarcodeGenerator
import com.project.iosephknecht.barcode_sender_plugin.domain.LocalStorage
import com.project.iosephknecht.barcode_sender_plugin.domain.SettingsStorage
import com.project.iosephknecht.barcode_sender_plugin.domain.StringProvider
import com.project.iosephknecht.barcode_sender_plugin.presentation.app.ApplicationDiComponent
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_generator.BarcodeGeneratorFeatureContract
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_generator.BarcodeGeneratorFeatureFactoryDefault
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.news
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.device_selector.DeviceSelectorFeatureContract
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.device_selector.DeviceSelectorFeatureFactoryDefault
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.devices.DevicesFeatureContract
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.devices.DevicesFeatureFactory
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.devices_selector.DevicesSelectorFeatureContract
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.devices_selector.DevicesSelectorFeatureFactoryDefault
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_editor.BarcodeEditorFeatureContract
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_editor.BarcodeEditorFeatureFactoryDefault
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.recent_barcode_type.RecentBarcodeTypeFeatureContract
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.recent_barcode_type.RecentBarcodeTypeFeatureFactoryDefault
import com.project.iosephknecht.barcode_sender_plugin.presentation.multiple_choice.component.MultipleChoiceListener
import com.project.iosephknecht.barcode_sender_plugin.presentation.sender.component.BarcodeComponent
import com.project.iosephknecht.barcode_sender_plugin.presentation.sender.component.BarcodeComponentDefault
import com.project.iosephknecht.barcode_sender_plugin.presentation.sender.view.MultipleChoiceDialogFactory
import com.project.iosephknecht.barcode_sender_plugin.presentation.sender.view.MultipleChoiceDialogFactoryDefault
import com.project.iosephknecht.barcode_sender_plugin.presentation.sender.view.BarcodeSenderDialog
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides

/**
 * Contract for DI-container [BarcodeComponent].
 *
 * @author IosephKnecht
 */
@BarcodeComponentScope
@Component(dependencies = [ApplicationDiComponent::class], modules = [BarcodeDiModule::class])
internal interface BarcodeDiComponent {

    @Component.Factory
    interface Factory {

        fun create(
            applicationDiComponent: ApplicationDiComponent,
            @BindsInstance listener: MultipleChoiceListener
        ): BarcodeDiComponent
    }

    fun inject(dialog: BarcodeSenderDialog)
}

/**
 * Module for DI-container [BarcodeComponent].
 *
 * @author IosephKnecht
 */
@Module
internal class BarcodeDiModule {

    @Provides
    @BarcodeComponentScope
    fun provideLogger(): Logger {
        return Logger.getInstance(BarcodeComponentDefault::class.java)
    }

    @BarcodeComponentScope
    @Provides
    fun provideDevicesFeature(): DevicesFeatureContract.Feature {
        return DevicesFeatureFactory().create()
    }

    @BarcodeComponentScope
    @Provides
    fun provideBarcodeGeneratorFeature(barcodeGenerator: BarcodeGenerator): BarcodeGeneratorFeatureContract.Feature {
        return BarcodeGeneratorFeatureFactoryDefault(barcodeGenerator).create()
    }

    @BarcodeComponentScope
    @Provides
    fun provideRecentBarcodeTypeFeature(
        localStorage: LocalStorage,
        barcodeGeneratorFeature: BarcodeGeneratorFeatureContract.Feature
    ): RecentBarcodeTypeFeatureContract.Feature {
        return RecentBarcodeTypeFeatureFactoryDefault(
            localStorage,
            barcodeGeneratorFeature.news
        ).create()
    }

    @BarcodeComponentScope
    @Provides
    fun provideDevicesSelectorFeature(
        devicesFeature: DevicesFeatureContract.Feature,
        deviceSelectorFeature: DeviceSelectorFeatureContract.Feature
    ): DevicesSelectorFeatureContract.Feature {
        return DevicesSelectorFeatureFactoryDefault(
            devicesFeature = devicesFeature,
            deviceSelector = deviceSelectorFeature
        ).create()
    }

    @BarcodeComponentScope
    @Provides
    fun provideDeviceSelectorFeature(
        devicesFeature: DevicesFeatureContract.Feature,
        settingsStorage: SettingsStorage
    ): DeviceSelectorFeatureContract.Feature {
        return DeviceSelectorFeatureFactoryDefault(
            devicesFeature = devicesFeature,
            settingsStorage = settingsStorage
        ).create()
    }

    @BarcodeComponentScope
    @Provides
    fun provideBarcodeEditorFeature(
        localStorage: LocalStorage,
        barcodeGeneratorFeature: BarcodeGeneratorFeatureContract.Feature
    ): BarcodeEditorFeatureContract.Feature {
        return BarcodeEditorFeatureFactoryDefault(localStorage, barcodeGeneratorFeature.news).create()
    }

    @BarcodeComponentScope
    @Provides
    fun provideMultipleChoiceDialogFactory(
        devicesSelectorFeature: DevicesSelectorFeatureContract.Feature,
        devicesFeature: DevicesFeatureContract.Feature,
        listener: MultipleChoiceListener
    ): MultipleChoiceDialogFactory {
        return MultipleChoiceDialogFactoryDefault(
            devicesSelectorFeature = devicesSelectorFeature,
            devicesFeature = devicesFeature,
            listener = listener
        )
    }

    @BarcodeComponentScope
    @Provides
    fun provideBarcodeComponent(
        stringProvider: StringProvider,
        devicesFeature: DevicesFeatureContract.Feature,
        devicesSelectorFeature: DevicesSelectorFeatureContract.Feature,
        deviceSelectorFeature: DeviceSelectorFeatureContract.Feature,
        barcodeEditorFeature: BarcodeEditorFeatureContract.Feature,
        barcodeGeneratorFeature: BarcodeGeneratorFeatureContract.Feature,
        recentBarcodeTypeFeature: RecentBarcodeTypeFeatureContract.Feature,
        logger: Logger
    ): BarcodeComponent {
        return BarcodeComponentDefault(
            stringProvider = stringProvider,
            devicesFeature = devicesFeature,
            devicesSelectorFeature = devicesSelectorFeature,
            deviceSelectorFeature = deviceSelectorFeature,
            barcodeEditorFeature = barcodeEditorFeature,
            barcodeGeneratorFeature = barcodeGeneratorFeature,
            recentBarcodeTypeFeature = recentBarcodeTypeFeature,
            logger = logger
        )
    }
}