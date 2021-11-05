package com.project.iosephknecht.barcode_sender_plugin.presentation.features.device_selector

import com.project.iosephknecht.barcode_sender_plugin.presentation.features.device_selector.DeviceSelectorFeatureContract.State
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.device_selector.DeviceSelectorFeatureContract.Effect
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.device_selector.DeviceSelectorFeatureContract.News

/**
 * Implementation news mapper for device selector feature.
 *
 * @author IosephKnecht
 */
internal class DeviceSelectorNewsMapperDefault : DeviceSelectorFeatureContract.NewsMapper {

    override fun map(
        state: State,
        effect: Effect
    ): News? {
        return when (state) {
            State.NotInitialized -> when (effect) {
                Effect.CancelChoiceMultipleDevice,
                Effect.ResetSelectedDevice,
                Effect.StartChoiceMultipleDevice,
                Effect.StartInitialization,
                Effect.SuccessSendBarcode,
                is Effect.ChangeSelectedDevice,
                is Effect.FailureInitialization,
                is Effect.FailureSendBarcode,
                is Effect.StartSendBarcode,
                is Effect.SuccessChoiceMultipleDevice,
                is Effect.SuccessInitialization -> null
            }
            State.ProcessInitialize -> when (effect) {
                Effect.CancelChoiceMultipleDevice,
                Effect.ResetSelectedDevice,
                Effect.StartChoiceMultipleDevice,
                Effect.StartInitialization,
                Effect.SuccessSendBarcode,
                is Effect.ChangeSelectedDevice,
                is Effect.FailureInitialization,
                is Effect.FailureSendBarcode,
                is Effect.StartSendBarcode,
                is Effect.SuccessChoiceMultipleDevice,
                is Effect.SuccessInitialization -> null
            }
            is State.FailureInitialize -> when (effect) {
                Effect.CancelChoiceMultipleDevice,
                Effect.ResetSelectedDevice,
                Effect.StartChoiceMultipleDevice,
                Effect.StartInitialization,
                Effect.SuccessSendBarcode,
                is Effect.ChangeSelectedDevice,
                is Effect.FailureInitialization,
                is Effect.FailureSendBarcode,
                is Effect.StartSendBarcode,
                is Effect.SuccessChoiceMultipleDevice,
                is Effect.SuccessInitialization -> null
            }
            is State.ProcessSendBarcode -> when (effect) {
                Effect.ResetSelectedDevice,
                Effect.CancelChoiceMultipleDevice,
                Effect.StartChoiceMultipleDevice,
                Effect.StartInitialization,
                is Effect.FailureInitialization,
                is Effect.SuccessInitialization,
                is Effect.SuccessChoiceMultipleDevice,
                is Effect.ChangeSelectedDevice,
                is Effect.StartSendBarcode -> null
                is Effect.FailureSendBarcode -> News.FailureSendCode(effect.throwable)
                Effect.SuccessSendBarcode -> News.SuccessfulSendCode
            }
            is State.NotHaveSelection -> when (effect) {
                Effect.StartChoiceMultipleDevice -> News.MultipleChoiceDevice
                is Effect.ChangeSelectedDevice -> News.SingleChoiceDevice(effect.device)
                Effect.CancelChoiceMultipleDevice,
                Effect.SuccessSendBarcode,
                Effect.ResetSelectedDevice,
                Effect.StartInitialization,
                is Effect.FailureInitialization,
                is Effect.SuccessInitialization,
                is Effect.FailureSendBarcode,
                is Effect.StartSendBarcode,
                is Effect.SuccessChoiceMultipleDevice -> null
            }
            is State.HaveSelection -> when (effect) {
                Effect.StartChoiceMultipleDevice -> News.MultipleChoiceDevice
                Effect.ResetSelectedDevice -> News.ResetSelected
                is Effect.ChangeSelectedDevice -> News.SingleChoiceDevice(effect.device)
                Effect.CancelChoiceMultipleDevice,
                Effect.SuccessSendBarcode,
                Effect.StartInitialization,
                is Effect.FailureInitialization,
                is Effect.SuccessInitialization,
                is Effect.FailureSendBarcode,
                is Effect.StartSendBarcode,
                is Effect.SuccessChoiceMultipleDevice -> null
            }
            is State.HaveMultipleChoiceDevices -> when (effect) {
                Effect.StartChoiceMultipleDevice -> News.MultipleChoiceDevice
                Effect.ResetSelectedDevice -> News.ResetSelected
                is Effect.ChangeSelectedDevice -> News.SingleChoiceDevice(effect.device)
                Effect.CancelChoiceMultipleDevice,
                Effect.SuccessSendBarcode,
                Effect.ResetSelectedDevice,
                Effect.StartInitialization,
                is Effect.FailureInitialization,
                is Effect.SuccessInitialization,
                is Effect.FailureSendBarcode,
                is Effect.StartSendBarcode,
                is Effect.SuccessChoiceMultipleDevice -> null
            }
            is State.ProcessSendBarcodeMultiple -> when (effect) {
                Effect.SuccessSendBarcode -> News.SuccessfulSendCode
                is Effect.FailureSendBarcode -> News.FailureSendCode(effect.throwable)
                Effect.StartChoiceMultipleDevice,
                Effect.CancelChoiceMultipleDevice,
                Effect.ResetSelectedDevice,
                Effect.StartInitialization,
                is Effect.FailureInitialization,
                is Effect.SuccessInitialization,
                is Effect.ChangeSelectedDevice,
                is Effect.StartSendBarcode,
                is Effect.SuccessChoiceMultipleDevice -> null
            }
            is State.WaitingChoiceMultipleDevice -> when (effect) {
                Effect.ResetSelectedDevice,
                Effect.CancelChoiceMultipleDevice -> News.ResetSelected
                is Effect.ChangeSelectedDevice -> News.SingleChoiceDevice(effect.device)
                Effect.StartChoiceMultipleDevice,
                Effect.SuccessSendBarcode,
                Effect.ResetSelectedDevice,
                Effect.StartInitialization,
                is Effect.FailureInitialization,
                is Effect.SuccessInitialization,
                is Effect.FailureSendBarcode,
                is Effect.StartSendBarcode,
                is Effect.SuccessChoiceMultipleDevice -> null
            }
        }
    }
}