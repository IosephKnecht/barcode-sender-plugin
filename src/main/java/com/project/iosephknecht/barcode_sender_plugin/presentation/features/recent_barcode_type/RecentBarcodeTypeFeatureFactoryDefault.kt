package com.project.iosephknecht.barcode_sender_plugin.presentation.features.recent_barcode_type

import com.project.iosephknecht.barcode_sender_plugin.domain.LocalStorage
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_generator.BarcodeGeneratorFeatureContract
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.Logger
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.SchedulersContainer
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.Swing
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.intellijIdeaLogger
import io.reactivex.rxjava3.core.Observable

/**
 * Factory for create default implementation [RecentBarcodeTypeFeatureContract.Feature].
 *
 * @param localStorage
 * @param barcodeGeneratorNews
 *
 * @author IosephKnecht
 */
internal class RecentBarcodeTypeFeatureFactoryDefault(
    private val localStorage: LocalStorage,
    private val barcodeGeneratorNews: Observable<BarcodeGeneratorFeatureContract.News>,
    private val schedulersContainer: SchedulersContainer = SchedulersContainer.Swing
) {

    private val reducer by lazy {
        RecentBarcodeTypeReducerDefault()
    }

    private val producer by lazy {
        RecentBarcodeTypeProducerDefault(localStorage, schedulersContainer)
    }

    private val bootstrapper by lazy {
        RecentBarcodeTypeBootstrapperDefault(barcodeGeneratorNews)
    }

    private val actionMapper by lazy {
        RecentBarcodeTypeActionMapperDefault()
    }

    private val newsMapper by lazy {
        RecentBarcodeTypeNewsMapperDefault()
    }

    private val logger by lazy {
        Logger.intellijIdeaLogger(RecentBarcodeTypeFeatureDefault::class)
    }

    fun create(): RecentBarcodeTypeFeatureContract.Feature {
        return RecentBarcodeTypeFeatureDefault(
            reducer = reducer,
            producer = producer,
            actionMapper = actionMapper,
            newsMapper = newsMapper,
            bootstrapper = bootstrapper,
            logger = logger,
            schedulersContainer = schedulersContainer
        )
    }
}