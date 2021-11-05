package com.project.iosephknecht.barcode_sender_plugin.presentation.features.settings

import com.project.iosephknecht.barcode_sender_plugin.presentation.features.settings.SettingsFeatureContract.ActionMapper
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.settings.SettingsFeatureContract.State
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.settings.SettingsFeatureContract.Intent
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.settings.SettingsFeatureContract.Action

/**
 * Default implementation [ActionMapper].
 *
 * @author IosephKnecht
 */
internal class SettingsActionMapperDefault : ActionMapper {

    override fun map(
        state: State,
        intent: Intent
    ): Action? {
        return when (state) {
            State.NotInitialize -> when (intent) {
                Intent.LoadSettings -> Action.ExecuteLoadSettings
                Intent.ResetSettings,
                Intent.SaveSettings,
                is Intent.SetBarcodeReceiveKey -> null
            }
            State.ProcessInitialize -> when (intent) {
                Intent.LoadSettings,
                Intent.ResetSettings,
                Intent.SaveSettings,
                is Intent.SetBarcodeReceiveKey -> null
            }
            is State.FailureInitialize -> when (intent) {
                Intent.LoadSettings -> Action.ExecuteLoadSettings
                Intent.ResetSettings,
                Intent.SaveSettings,
                is Intent.SetBarcodeReceiveKey -> null
            }
            is State.Data -> when (intent) {
                is Intent.SetBarcodeReceiveKey -> Action.ExecuteSetBarcodeReceiveKey(intent.key)
                Intent.ResetSettings,
                Intent.SaveSettings,
                Intent.LoadSettings -> null
            }
            is State.ChangedSettings -> when (intent) {
                Intent.ResetSettings -> Action.ExecuteResetSettings
                Intent.SaveSettings -> Action.ExecuteSaveSettings
                is Intent.SetBarcodeReceiveKey -> Action.ExecuteSetBarcodeReceiveKey(intent.key)
                Intent.LoadSettings -> null
            }
            is State.ProcessSaveSettings -> when (intent) {
                Intent.LoadSettings,
                Intent.ResetSettings,
                Intent.SaveSettings,
                is Intent.SetBarcodeReceiveKey -> null
            }
        }
    }
}