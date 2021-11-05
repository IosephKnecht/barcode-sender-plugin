package com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_generator_multiple

import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_generator_multiple.BarcodeGeneratorMultipleFeatureContract.Reducer
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_generator_multiple.BarcodeGeneratorMultipleFeatureContract.State
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_generator_multiple.BarcodeGeneratorMultipleFeatureContract.Effect

/**
 * Default implementation [Reducer].
 *
 * @author IosephKnecht
 */
internal class BarcodeGeneratorMultipleReducerDefault : Reducer {

    override fun reduce(
        state: State,
        effect: Effect
    ): State {
        return when (state) {
            is State.Empty -> state + effect
            is State.ProcessGenerating -> state + effect
            is State.Data -> state + effect
        }
    }

    private operator fun State.Empty.plus(effect: Effect): State {
        return when (effect) {
            Effect.StartGenerateBarcodeList -> State.ProcessGenerating(
                currentBarcodeCount = this.currentBarcodeCount,
                minBarcodeCount = this.minBarcodeCount,
                maxBarcodeCount = this.maxBarcodeCount,
                barcodeType = this.barcodeType
            )
            is Effect.ChangeBarcodeCount -> copy(currentBarcodeCount = effect.barcodeCount)
            is Effect.ChangeBarcodeType -> copy(barcodeType = effect.barcodeType)
            Effect.FailureConstraintBarcodeCount,
            Effect.ApproveBarcodeList,
            is Effect.FailureGenerateBarcodeList,
            is Effect.SuccessGenerateBarcodeList -> this
        }
    }

    private operator fun State.ProcessGenerating.plus(effect: Effect): State {
        return when (effect) {
            is Effect.FailureGenerateBarcodeList -> State.Empty(
                currentBarcodeCount = this.currentBarcodeCount,
                minBarcodeCount = this.minBarcodeCount,
                maxBarcodeCount = this.maxBarcodeCount,
                barcodeType = this.barcodeType
            )
            is Effect.SuccessGenerateBarcodeList -> State.Data(
                currentBarcodeCount = this.currentBarcodeCount,
                minBarcodeCount = this.minBarcodeCount,
                maxBarcodeCount = this.maxBarcodeCount,
                barcodeType = this.barcodeType,
                barcodeList = effect.barcodeList
            )
            Effect.StartGenerateBarcodeList,
            Effect.FailureConstraintBarcodeCount,
            Effect.ApproveBarcodeList,
            is Effect.ChangeBarcodeCount,
            is Effect.ChangeBarcodeType -> this
        }
    }

    private operator fun State.Data.plus(effect: Effect): State {
        return when (effect) {
            Effect.StartGenerateBarcodeList -> State.ProcessGenerating(
                currentBarcodeCount = this.currentBarcodeCount,
                minBarcodeCount = this.minBarcodeCount,
                maxBarcodeCount = this.maxBarcodeCount,
                barcodeType = this.barcodeType
            )
            is Effect.ChangeBarcodeCount -> copy(currentBarcodeCount = effect.barcodeCount)
            is Effect.ChangeBarcodeType -> copy(barcodeType = effect.barcodeType)
            Effect.FailureConstraintBarcodeCount,
            Effect.ApproveBarcodeList,
            is Effect.FailureGenerateBarcodeList,
            is Effect.SuccessGenerateBarcodeList -> this
        }
    }
}