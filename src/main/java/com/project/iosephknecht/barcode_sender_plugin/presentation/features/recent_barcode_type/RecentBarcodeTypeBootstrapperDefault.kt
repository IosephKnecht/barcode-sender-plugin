package com.project.iosephknecht.barcode_sender_plugin.presentation.features.recent_barcode_type

import com.project.iosephknecht.barcode_sender_plugin.presentation.features.recent_barcode_type.RecentBarcodeTypeFeatureContract.Action
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.recent_barcode_type.RecentBarcodeTypeFeatureContract.Bootstrapper
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_generator.BarcodeGeneratorFeatureContract.News as BarcodeGeneratorNews

/**
 * Default implementation for [Bootstrapper].
 *
 * @param barcodeGeneratorNews observable of barcode generator news.
 *
 * @author IosephKnecht
 */
internal class RecentBarcodeTypeBootstrapperDefault(
    private val barcodeGeneratorNews: Observable<BarcodeGeneratorNews>
) : Bootstrapper {

    override fun subscribe(observer: Observer<in Action>) {
        barcodeGeneratorNews
            .ofType(BarcodeGeneratorNews.SuccessfulGenerateBarcode::class.java)
            .map { Action.ExecuteChoiceBarcodeType(it.barcodeType) }
            .subscribe(observer)
    }
}