package com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_generator

import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_generator.BarcodeGeneratorFeatureContract.Reducer
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_generator.BarcodeGeneratorFeatureContract.State
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_generator.BarcodeGeneratorFeatureContract.Effect

/**
 * Default implementation for [Reducer].
 *
 * @author IosephKnecht
 */
internal class BarcodeGeneratorReducerDefault : Reducer {

    override fun reduce(
        state: State,
        effect: Effect
    ): State {
        return when (state) {
            is State.Empty -> state + effect
            is State.ProcessGenerateBarcode -> state + effect
        }
    }

    private operator fun State.Empty.plus(effect: Effect): State {
        return when (effect) {
            is Effect.StartGenerateBarcode -> State.ProcessGenerateBarcode(barcodeType = effect.barcodeType)
            is Effect.FailureGenerateBarcode,
            is Effect.SuccessGenerateBarcode -> this
        }
    }

    private operator fun State.ProcessGenerateBarcode.plus(effect: Effect): State {
        return when (effect) {
            is Effect.SuccessGenerateBarcode,
            is Effect.FailureGenerateBarcode -> State.Empty
            is Effect.StartGenerateBarcode -> this
        }
    }
}