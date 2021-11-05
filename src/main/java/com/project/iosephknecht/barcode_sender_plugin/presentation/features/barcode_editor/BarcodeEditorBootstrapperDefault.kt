package com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_editor

import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_generator.BarcodeGeneratorFeatureContract.News as BarcodeGeneratorNews
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_editor.BarcodeEditorFeatureContract.Bootstrapper
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_editor.BarcodeEditorFeatureContract.Intent
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer

/**
 * Default implementation [Bootstrapper].
 *
 * @param barcodeGeneratorNews observable of news [BarcodeGeneratorNews].
 *
 * @author IosephKnecht
 */
internal class BarcodeEditorBootstrapperDefault(
    private val barcodeGeneratorNews: Observable<BarcodeGeneratorNews>
) : Bootstrapper {

    override fun subscribe(observer: Observer<in Intent>) {
        barcodeGeneratorNews
            .ofType(BarcodeGeneratorNews.SuccessfulGenerateBarcode::class.java)
            .map { Intent.AddText(it.barcode) }
            .subscribe(observer)
    }
}