package com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_generator

import com.project.iosephknecht.barcode_sender_plugin.data.BarcodeType
import com.project.iosephknecht.barcode_sender_plugin.domain.BarcodeGenerator
import io.reactivex.rxjava3.core.Observable
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_generator.BarcodeGeneratorFeatureContract.Producer
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_generator.BarcodeGeneratorFeatureContract.Action
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_generator.BarcodeGeneratorFeatureContract.Effect
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * Default implementation [Producer].
 *
 * @param barcodeGenerator
 *
 * @author IosephKnecht
 */
internal class BarcodeGeneratorProducerDefault(
    private val barcodeGenerator: BarcodeGenerator
) : Producer {

    override fun produce(action: Action): Observable<Effect> {
        return when (action) {
            is Action.ExecuteGenerateBarcodeType -> generateBarcode(action.barcodeType)
        }
    }

    private fun generateBarcode(barcodeType: BarcodeType): Observable<Effect> {
        return Observable.fromCallable { barcodeGenerator.generate(barcodeType) }
            .map<Effect> { Effect.SuccessGenerateBarcode(it) }
            .onErrorReturn { Effect.FailureGenerateBarcode(it) }
            .startWithItem(Effect.StartGenerateBarcode(barcodeType))
            .subscribeOn(Schedulers.computation())
    }
}