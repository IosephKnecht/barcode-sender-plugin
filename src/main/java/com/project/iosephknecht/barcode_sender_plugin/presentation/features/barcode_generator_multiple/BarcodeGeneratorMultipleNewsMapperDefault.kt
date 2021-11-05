package com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_generator_multiple

import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_generator_multiple.BarcodeGeneratorMultipleFeatureContract.NewsMapper
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_generator_multiple.BarcodeGeneratorMultipleFeatureContract.State
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_generator_multiple.BarcodeGeneratorMultipleFeatureContract.Effect
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_generator_multiple.BarcodeGeneratorMultipleFeatureContract.News

/**
 * Default implementation [NewsMapper].
 *
 * @author IosephKnecht
 */
internal class BarcodeGeneratorMultipleNewsMapperDefault : NewsMapper {

    override fun map(
        state: State,
        effect: Effect
    ): News? {
        return when (state) {
            is State.Empty -> when (effect) {
                Effect.FailureConstraintBarcodeCount -> News.FailureConstraintBarcodeCount
                Effect.ApproveBarcodeList -> News.ApproveBarcodeList(null)
                Effect.StartGenerateBarcodeList,
                is Effect.ChangeBarcodeCount,
                is Effect.ChangeBarcodeType,
                is Effect.SuccessGenerateBarcodeList,
                is Effect.FailureGenerateBarcodeList -> null
            }
            is State.Data -> when (effect) {
                Effect.FailureConstraintBarcodeCount -> News.FailureConstraintBarcodeCount
                Effect.ApproveBarcodeList -> News.ApproveBarcodeList(state.barcodeList)
                Effect.StartGenerateBarcodeList,
                is Effect.ChangeBarcodeCount,
                is Effect.ChangeBarcodeType,
                is Effect.SuccessGenerateBarcodeList,
                is Effect.FailureGenerateBarcodeList -> null
            }
            is State.ProcessGenerating -> when (effect) {
                Effect.ApproveBarcodeList -> News.ApproveBarcodeList(null)
                is Effect.FailureGenerateBarcodeList -> News.FailureGenerateBarcodeList(effect.throwable)
                Effect.StartGenerateBarcodeList,
                Effect.FailureConstraintBarcodeCount,
                is Effect.ChangeBarcodeCount,
                is Effect.ChangeBarcodeType,
                is Effect.SuccessGenerateBarcodeList -> null
            }
        }
    }
}