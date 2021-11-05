package com.project.iosephknecht.barcode_sender_plugin.presentation.features.settings

import com.project.iosephknecht.barcode_sender_plugin.presentation.features.settings.SettingsFeatureContract.Reducer
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.settings.SettingsFeatureContract.State
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.settings.SettingsFeatureContract.Effect

/**
 * Default implementation [Reducer].
 *
 * @author IosephKnecht
 */
internal class SettingsReducerDefault : Reducer {

    override fun reduce(
        state: State,
        effect: Effect
    ): State {
        return when (state) {
            is State.NotInitialize -> state + effect
            is State.ProcessInitialize -> state + effect
            is State.FailureInitialize -> state + effect
            is State.Data -> state + effect
            is State.ChangedSettings -> state + effect
            is State.ProcessSaveSettings -> state + effect
        }
    }

    private operator fun State.NotInitialize.plus(effect: Effect): State {
        return when (effect) {
            Effect.StartLoadSettings -> State.ProcessInitialize
            Effect.StartSaveSettings,
            Effect.ResetSettings,
            is Effect.SuccessSaveSettings,
            is Effect.FailureLoadSettings,
            is Effect.FailureSaveSettings,
            is Effect.SuccessLoadSettings,
            is Effect.SetBarcodeReceiveKey -> this
        }
    }

    private operator fun State.ProcessInitialize.plus(effect: Effect): State {
        return when (effect) {
            is Effect.FailureLoadSettings -> State.FailureInitialize(effect.throwable)
            is Effect.SuccessLoadSettings -> State.Data(effect.receiverKey)
            Effect.StartLoadSettings,
            Effect.StartSaveSettings,
            Effect.ResetSettings,
            is Effect.SuccessSaveSettings,
            is Effect.FailureSaveSettings,
            is Effect.SetBarcodeReceiveKey -> this
        }
    }

    private operator fun State.FailureInitialize.plus(effect: Effect): State {
        return when (effect) {
            Effect.StartLoadSettings -> State.ProcessInitialize
            Effect.StartSaveSettings,
            Effect.ResetSettings,
            is Effect.SuccessSaveSettings,
            is Effect.FailureLoadSettings,
            is Effect.SuccessLoadSettings,
            is Effect.SetBarcodeReceiveKey,
            is Effect.FailureSaveSettings -> this
        }
    }

    private operator fun State.Data.plus(effect: Effect): State {
        return when (effect) {
            is Effect.SetBarcodeReceiveKey -> State.ChangedSettings(
                originalBarcodeReceiverKey = this.barcodeReceiverKey,
                changedBarcodeReceiverKey = effect.key
            )
            Effect.StartLoadSettings,
            Effect.StartSaveSettings,
            Effect.ResetSettings,
            is Effect.FailureLoadSettings,
            is Effect.FailureSaveSettings,
            is Effect.SuccessLoadSettings,
            is Effect.SuccessSaveSettings -> this
        }
    }

    private operator fun State.ChangedSettings.plus(effect: Effect): State {
        return when (effect) {
            Effect.StartSaveSettings -> State.ProcessSaveSettings(
                originalBarcodeReceiverKey = originalBarcodeReceiverKey,
                changedBarcodeReceiverKey = changedBarcodeReceiverKey
            )
            Effect.ResetSettings -> State.Data(originalBarcodeReceiverKey)
            is Effect.SetBarcodeReceiveKey -> copy(changedBarcodeReceiverKey = effect.key)
            Effect.StartLoadSettings,
            is Effect.SuccessSaveSettings,
            is Effect.FailureLoadSettings,
            is Effect.FailureSaveSettings,
            is Effect.SuccessLoadSettings -> this
        }
    }

    private operator fun State.ProcessSaveSettings.plus(effect: Effect): State {
        return when (effect) {
            is Effect.SuccessSaveSettings -> State.Data(effect.receiverKey)
            is Effect.FailureSaveSettings -> State.ChangedSettings(
                originalBarcodeReceiverKey = originalBarcodeReceiverKey,
                changedBarcodeReceiverKey = changedBarcodeReceiverKey
            )
            Effect.StartSaveSettings,
            Effect.StartLoadSettings,
            Effect.ResetSettings,
            is Effect.FailureLoadSettings,
            is Effect.SetBarcodeReceiveKey,
            is Effect.SuccessLoadSettings -> this
        }
    }
}