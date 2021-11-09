package com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_generator_multiple

import com.project.iosephknecht.barcode_sender_plugin.data.BarcodeType
import com.project.iosephknecht.barcode_sender_plugin.domain.BarcodeGenerator
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_generator_multiple.BarcodeGeneratorMultipleFeatureContract.State
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.Logger
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.SchedulersContainer
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.Swing
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.intellijIdeaLogger

/**
 * Factory for create default implementation [BarcodeGeneratorMultipleFeatureContract.Feature].
 *
 * @param minCount minimum value for barcode count.
 * @param maxCount maximum value for barcode count.
 * @param currentCount current value for barcode count.
 * @param currentBarcodeType current value for barcode type.
 * @param barcodeGenerator instance of generator.
 *
 * @author IosephKnecht
 */
internal class BarcodeGeneratorMultipleFeatureFactoryDefault(
    private val minCount: Int,
    private val maxCount: Int,
    private val currentCount: Int,
    private val currentBarcodeType: BarcodeType,
    private val barcodeGenerator: BarcodeGenerator,
    private val schedulersContainer: SchedulersContainer = SchedulersContainer.Swing
) {

    private val reducer by lazy {
        BarcodeGeneratorMultipleReducerDefault()
    }

    private val producer by lazy {
        BarcodeGeneratorMultipleProducerDefault(barcodeGenerator, schedulersContainer)
    }

    private val actionMapper by lazy {
        BarcodeGeneratorMultipleActionMapperDefault()
    }

    private val newsMapper by lazy {
        BarcodeGeneratorMultipleNewsMapperDefault()
    }

    private val logger by lazy {
        Logger.intellijIdeaLogger(BarcodeGeneratorMultipleFeatureDefault::class)
    }

    private val initialState by lazy {
        State.Empty(
            minBarcodeCount = minCount,
            maxBarcodeCount = maxCount,
            currentBarcodeCount = currentCount,
            barcodeType = currentBarcodeType
        )
    }

    fun create(): BarcodeGeneratorMultipleFeatureContract.Feature {
        return BarcodeGeneratorMultipleFeatureDefault(
            initialState = initialState,
            reducer = reducer,
            producer = producer,
            actionMapper = actionMapper,
            newsMapper = newsMapper,
            logger = logger,
            schedulersContainer = schedulersContainer
        )
    }
}