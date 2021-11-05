package com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_editor

import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_editor.BarcodeEditorFeatureContract.Reducer
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_editor.BarcodeEditorFeatureContract.State
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_editor.BarcodeEditorFeatureContract.Effect

/**
 * Default implementation for [BarcodeEditorFeatureContract.Reducer].
 *
 * @author IosephKnecht
 */
internal class BarcodeEditorReducerDefault : Reducer {

    override fun reduce(
        state: State,
        effect: Effect
    ): State {
        return when (state) {
            is State.NotInitialized -> state + effect
            is State.Initialized -> state + effect
        }
    }

    private operator fun State.NotInitialized.plus(effect: Effect): State {
        return when (effect) {
            is Effect.FailureEdit,
            is Effect.SuccessEdit,
            is Effect.SuccessAddText -> this
            is Effect.SuccessInitialize -> State.Initialized(effect.initialText)
            is Effect.FailureInitialize -> State.Initialized("")
        }
    }

    private operator fun State.Initialized.plus(effect: Effect): State {
        return when (effect) {
            is Effect.FailureEdit,
            is Effect.FailureInitialize,
            is Effect.SuccessInitialize -> this
            is Effect.SuccessAddText -> copy(barcodeText = effect.barcode)
            is Effect.SuccessEdit -> copy(barcodeText = effect.barcode)
        }
    }
}