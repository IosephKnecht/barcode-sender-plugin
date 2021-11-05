package com.project.iosephknecht.barcode_sender_plugin.presentation.features.device_selector

import com.project.iosephknecht.barcode_sender_plugin.presentation.features.device_selector.DeviceSelectorFeatureContract.ActionMapper
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.device_selector.DeviceSelectorFeatureContract.State
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.device_selector.DeviceSelectorFeatureContract.Intent
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.device_selector.DeviceSelectorFeatureContract.Action

/**
 * Implementation action mapper for device selector feature.
 *
 * @author IosephKnecht
 */
internal class DeviceSelectorFeatureActionMapperDefault : ActionMapper {

    override fun map(
        state: State,
        intent: Intent
    ): Action? {
        return when (state) {
            State.NotInitialized -> when (intent) {
                Intent.Initialize -> Action.ExecuteInitialize
                Intent.CancelMultipleChoice,
                Intent.ChoiceMultipleDevices,
                Intent.ResetSelected,
                is Intent.SelectDevice,
                is Intent.SendBarcode,
                is Intent.SuccessMultipleChoice -> null
            }
            State.ProcessInitialize -> when (intent) {
                Intent.CancelMultipleChoice,
                Intent.ChoiceMultipleDevices,
                Intent.Initialize,
                Intent.ResetSelected,
                is Intent.SelectDevice,
                is Intent.SendBarcode,
                is Intent.SuccessMultipleChoice -> null
            }
            is State.FailureInitialize -> when (intent) {
                Intent.Initialize -> Action.ExecuteInitialize
                Intent.CancelMultipleChoice,
                Intent.ChoiceMultipleDevices,
                Intent.ResetSelected,
                is Intent.SelectDevice,
                is Intent.SendBarcode,
                is Intent.SuccessMultipleChoice -> null
            }
            is State.NotHaveSelection -> when (intent) {
                Intent.Initialize,
                Intent.ResetSelected,
                Intent.CancelMultipleChoice,
                is Intent.SuccessMultipleChoice,
                is Intent.SendBarcode -> null
                Intent.ChoiceMultipleDevices -> Action.ExecuteMultipleChoiceDevice
                is Intent.SelectDevice -> Action.ExecuteSelectDevice(intent.device)
            }
            is State.WaitingChoiceMultipleDevice -> when (intent) {
                Intent.CancelMultipleChoice -> Action.ExecuteCancelMultipleChoiceDevice
                Intent.ResetSelected -> Action.ExecuteResetSelectDevice
                is Intent.SelectDevice -> Action.ExecuteSelectDevice(intent.device)
                is Intent.SuccessMultipleChoice -> Action.ExecuteSuccessChoiceMultipleDevice(intent.devices)
                Intent.Initialize,
                Intent.ChoiceMultipleDevices,
                is Intent.SendBarcode -> null
            }
            is State.HaveSelection -> when (intent) {
                Intent.ResetSelected -> Action.ExecuteResetSelectDevice
                Intent.ChoiceMultipleDevices -> Action.ExecuteMultipleChoiceDevice
                is Intent.SendBarcode -> Action.ExecuteSendBarcode(intent.value)
                is Intent.SelectDevice -> Action.ExecuteSelectDevice(intent.device)
                Intent.Initialize,
                Intent.CancelMultipleChoice,
                is Intent.SuccessMultipleChoice -> null
            }
            is State.ProcessSendBarcode -> when (intent) {
                Intent.Initialize,
                Intent.ResetSelected,
                Intent.ChoiceMultipleDevices,
                Intent.CancelMultipleChoice,
                is Intent.SuccessMultipleChoice,
                is Intent.SelectDevice,
                is Intent.SendBarcode -> null
            }
            is State.HaveMultipleChoiceDevices -> when (intent) {
                Intent.ChoiceMultipleDevices -> Action.ExecuteMultipleChoiceDevice
                Intent.ResetSelected -> Action.ExecuteResetSelectDevice
                is Intent.SelectDevice -> Action.ExecuteSelectDevice(intent.device)
                is Intent.SendBarcode -> Action.ExecuteSendBarcode(intent.value)
                Intent.Initialize,
                Intent.CancelMultipleChoice,
                is Intent.SuccessMultipleChoice -> null
            }
            is State.ProcessSendBarcodeMultiple -> when (intent) {
                Intent.ChoiceMultipleDevices,
                Intent.ResetSelected,
                Intent.CancelMultipleChoice,
                Intent.Initialize,
                is Intent.SuccessMultipleChoice,
                is Intent.SelectDevice,
                is Intent.SendBarcode -> null
            }
        }
    }
}