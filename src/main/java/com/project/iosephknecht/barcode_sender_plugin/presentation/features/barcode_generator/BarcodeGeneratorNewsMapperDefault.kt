package com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_generator

import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_generator.BarcodeGeneratorFeatureContract.NewsMapper
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_generator.BarcodeGeneratorFeatureContract.State
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_generator.BarcodeGeneratorFeatureContract.Effect
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_generator.BarcodeGeneratorFeatureContract.News

/**
 * Default implementation [NewsMapper].
 *
 * @author IosephKnecht
 */
internal class BarcodeGeneratorNewsMapperDefault : NewsMapper {

    override fun map(
        state: State,
        effect: Effect
    ): News? {
        return when (state) {
            State.Empty -> when (effect) {
                is Effect.StartGenerateBarcode,
                is Effect.FailureGenerateBarcode,
                is Effect.SuccessGenerateBarcode -> null
            }
            is State.ProcessGenerateBarcode -> when (effect) {
                is Effect.FailureGenerateBarcode -> News.FailureGenerateBarcode(
                    state.barcodeType,
                    effect.throwable
                )
                is Effect.SuccessGenerateBarcode -> News.SuccessfulGenerateBarcode(
                    effect.barcode,
                    state.barcodeType
                )
                is Effect.StartGenerateBarcode -> null
            }
        }
    }
}