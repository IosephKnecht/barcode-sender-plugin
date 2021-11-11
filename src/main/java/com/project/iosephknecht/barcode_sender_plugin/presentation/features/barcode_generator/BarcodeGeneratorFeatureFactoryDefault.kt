package com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_generator

import com.project.iosephknecht.barcode_sender_plugin.domain.BarcodeGenerator
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_generator.BarcodeGeneratorFeatureContract.*
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.Logger
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.SchedulersContainer
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.Swing
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.intellijIdeaLogger

/**
 * Default implementation factory for create [Feature].
 *
 * @param barcodeGenerator
 *
 * @author IosephKnecht
 */
internal class BarcodeGeneratorFeatureFactoryDefault(
    private val barcodeGenerator: BarcodeGenerator,
    private val schedulersContainer: SchedulersContainer = SchedulersContainer.Swing
) {

    private val reducer by lazy {
        BarcodeGeneratorReducerDefault()
    }

    private val producer by lazy {
        BarcodeGeneratorProducerDefault(barcodeGenerator, schedulersContainer)
    }

    private val actionMapper by lazy {
        BarcodeGeneratorActionMapperDefault()
    }

    private val newsMapper by lazy {
        BarcodeGeneratorNewsMapperDefault()
    }

    private val logger by lazy {
        Logger.intellijIdeaLogger(BarcodeGeneratorFeatureDefault::class)
    }

    fun create(): Feature {
        return BarcodeGeneratorFeatureDefault(
            reducer = reducer,
            producer = producer,
            actionMapper = actionMapper,
            newsMapper = newsMapper,
            logger = logger,
            schedulersContainer = schedulersContainer
        )
    }
}