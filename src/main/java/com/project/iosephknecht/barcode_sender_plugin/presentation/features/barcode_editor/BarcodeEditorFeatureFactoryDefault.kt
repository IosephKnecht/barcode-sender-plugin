package com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_editor

import com.project.iosephknecht.barcode_sender_plugin.domain.LocalStorage
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_generator.BarcodeGeneratorFeatureContract
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.Logger
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.intellijIdeaLogger
import io.reactivex.rxjava3.core.Observable

/**
 * Factory for build default implementation [BarcodeEditorFeatureContract.Feature].
 *
 * @param localStorage
 *
 * @author IosephKnecht
 */
internal class BarcodeEditorFeatureFactoryDefault(
    private val localStorage: LocalStorage,
    private val barcodeGeneratorNews: Observable<BarcodeGeneratorFeatureContract.News>
) {

    private val reducer by lazy {
        BarcodeEditorReducerDefault()
    }

    private val producer by lazy {
        BarcodeEditorProducerDefault(localStorage)
    }

    private val bootstrapper by lazy {
        BarcodeEditorBootstrapperDefault(barcodeGeneratorNews)
    }

    private val logger by lazy {
        Logger.intellijIdeaLogger(BarcodeEditorFeatureDefault::class)
    }

    fun create(): BarcodeEditorFeatureContract.Feature {
        return BarcodeEditorFeatureDefault(
            reducer = reducer,
            producer = producer,
            bootstrapper = bootstrapper,
            logger = logger
        )
    }
}