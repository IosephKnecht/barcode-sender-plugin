package com.project.iosephknecht.barcode_sender_plugin.presentation.features.settings

import com.project.iosephknecht.barcode_sender_plugin.presentation.features.settings.SettingsFeatureContract.NewsMapper
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.settings.SettingsFeatureContract.State
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.settings.SettingsFeatureContract.Effect
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.settings.SettingsFeatureContract.News

/**
 * Default implementation [NewsMapper].
 *
 * @author IosephKnecht
 */
internal class SettingsNewsMapperDefault : NewsMapper {

    override fun map(
        state: State,
        effect: Effect
    ): News? {
        return when (state) {
            State.NotInitialize -> when (effect) {
                Effect.StartLoadSettings,
                Effect.StartSaveSettings,
                Effect.ResetSettings,
                is Effect.FailureLoadSettings,
                is Effect.FailureSaveSettings,
                is Effect.SetBarcodeReceiveKey,
                is Effect.SuccessLoadSettings,
                is Effect.SuccessSaveSettings -> null
            }
            State.ProcessInitialize -> when (effect) {
                Effect.StartLoadSettings,
                Effect.StartSaveSettings,
                Effect.ResetSettings,
                is Effect.FailureLoadSettings,
                is Effect.FailureSaveSettings,
                is Effect.SetBarcodeReceiveKey,
                is Effect.SuccessLoadSettings,
                is Effect.SuccessSaveSettings -> null
            }
            is State.FailureInitialize -> when (effect) {
                Effect.StartLoadSettings,
                Effect.StartSaveSettings,
                Effect.ResetSettings,
                is Effect.FailureLoadSettings,
                is Effect.FailureSaveSettings,
                is Effect.SetBarcodeReceiveKey,
                is Effect.SuccessLoadSettings,
                is Effect.SuccessSaveSettings -> null
            }
            is State.Data -> when (effect) {
                Effect.StartLoadSettings,
                Effect.StartSaveSettings,
                Effect.ResetSettings,
                is Effect.FailureLoadSettings,
                is Effect.FailureSaveSettings,
                is Effect.SetBarcodeReceiveKey,
                is Effect.SuccessLoadSettings,
                is Effect.SuccessSaveSettings -> null
            }
            is State.ChangedSettings -> when (effect) {
                Effect.StartLoadSettings,
                Effect.StartSaveSettings,
                Effect.ResetSettings,
                is Effect.FailureLoadSettings,
                is Effect.FailureSaveSettings,
                is Effect.SetBarcodeReceiveKey,
                is Effect.SuccessLoadSettings,
                is Effect.SuccessSaveSettings -> null
            }
            is State.ProcessSaveSettings -> when (effect) {
                is Effect.FailureSaveSettings -> News.FailureSaveSettings(effect.throwable)
                Effect.StartLoadSettings,
                Effect.StartSaveSettings,
                Effect.ResetSettings,
                is Effect.FailureLoadSettings,
                is Effect.SetBarcodeReceiveKey,
                is Effect.SuccessLoadSettings,
                is Effect.SuccessSaveSettings -> null
            }
        }
    }
}