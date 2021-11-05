package com.project.iosephknecht.barcode_sender_plugin.presentation.multiple_generator.di

import com.intellij.openapi.diagnostic.Logger
import com.project.iosephknecht.barcode_sender_plugin.data.BarcodeType
import com.project.iosephknecht.barcode_sender_plugin.domain.BarcodeGenerator
import com.project.iosephknecht.barcode_sender_plugin.presentation.app.ApplicationDiComponent
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_generator_multiple.BarcodeGeneratorMultipleFeatureContract
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_generator_multiple.BarcodeGeneratorMultipleFeatureFactoryDefault
import com.project.iosephknecht.barcode_sender_plugin.presentation.multiple_generator.component.MultipleBarcodeGeneratorComponent
import com.project.iosephknecht.barcode_sender_plugin.presentation.multiple_generator.component.MultipleBarcodeGeneratorComponentDefault
import dagger.Component
import dagger.Module
import dagger.Provides

/**
 * Contract for DI-container [MultipleBarcodeGeneratorComponent].
 *
 * @author IosephKnecht
 */
@MultipleBarcodeGeneratorDiScope
@Component(
    modules = [MultipleBarcodeGeneratorComponentModule::class],
    dependencies = [ApplicationDiComponent::class]
)
internal interface MultipleBarcodeGeneratorDiComponent {

    val component: MultipleBarcodeGeneratorComponent

    @Component.Factory
    interface Factory {

        fun create(applicationDiComponent: ApplicationDiComponent): MultipleBarcodeGeneratorDiComponent
    }
}


/**
 * Module for DI-container [MultipleBarcodeGeneratorDiComponent].
 *
 * @author IosephKnecht
 */
@Module
internal class MultipleBarcodeGeneratorComponentModule {

    @Provides
    @MultipleBarcodeGeneratorDiScope
    fun provideLogger(): Logger {
        return Logger.getInstance(MultipleBarcodeGeneratorComponentDefault::class.java)
    }

    @Provides
    @MultipleBarcodeGeneratorDiScope
    fun provideBarcodeGeneratorMultipleFeatureContract(
        barcodeGenerator: BarcodeGenerator
    ): BarcodeGeneratorMultipleFeatureContract.Feature {
        return BarcodeGeneratorMultipleFeatureFactoryDefault(
            minCount = 1,
            maxCount = 100,
            currentCount = 1,
            currentBarcodeType = BarcodeType.EAN8,
            barcodeGenerator = barcodeGenerator
        ).create()
    }

    @Provides
    @MultipleBarcodeGeneratorDiScope
    fun provideComponent(
        multipleBarcodeGeneratorMultipleFeature: BarcodeGeneratorMultipleFeatureContract.Feature,
        logger: Logger
    ): MultipleBarcodeGeneratorComponent {
        return MultipleBarcodeGeneratorComponentDefault(
            multipleBarcodeGeneratorFeature = multipleBarcodeGeneratorMultipleFeature,
            logger = logger
        )
    }
}
