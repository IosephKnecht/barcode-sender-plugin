package com.project.iosephknecht.barcode_sender_plugin.presentation.features.device_selector

import com.project.iosephknecht.barcode_sender_plugin.presentation.features.device_selector.DeviceSelectorFeatureContract.Reducer
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.device_selector.DeviceSelectorFeatureContract.State
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.device_selector.DeviceSelectorFeatureContract.Effect

/**
 * Implementation state reducer for device selector feature.
 *
 * @author IosephKnecht
 */
internal class DeviceSelectorFeatureReducerDefault : Reducer {

    override fun reduce(
        state: State,
        effect: Effect
    ): State {
        return when (state) {
            is State.NotInitialized -> state + effect
            is State.ProcessInitialize -> state + effect
            is State.FailureInitialize -> state + effect
            is State.NotHaveSelection -> state + effect
            is State.HaveSelection -> state + effect
            is State.ProcessSendBarcode -> state + effect
            is State.HaveMultipleChoiceDevices -> state + effect
            is State.ProcessSendBarcodeMultiple -> state + effect
            is State.WaitingChoiceMultipleDevice -> state + effect
        }
    }

    private operator fun State.NotInitialized.plus(effect: Effect): State {
        return when (effect) {
            Effect.StartInitialization -> State.ProcessInitialize
            Effect.CancelChoiceMultipleDevice,
            Effect.ResetSelectedDevice,
            Effect.StartChoiceMultipleDevice,
            Effect.SuccessSendBarcode,
            is Effect.ChangeSelectedDevice,
            is Effect.FailureInitialization,
            is Effect.FailureSendBarcode,
            is Effect.StartSendBarcode,
            is Effect.SuccessChoiceMultipleDevice,
            is Effect.SuccessInitialization -> this
        }
    }

    private operator fun State.ProcessInitialize.plus(effect: Effect): State {
        return when (effect) {
            is Effect.FailureInitialization -> State.FailureInitialize(effect.throwable, effect.errorReason)
            is Effect.SuccessInitialization -> State.NotHaveSelection(effect.barcodeReceiverKey)
            Effect.ResetSelectedDevice,
            Effect.StartChoiceMultipleDevice,
            Effect.StartInitialization,
            Effect.CancelChoiceMultipleDevice,
            Effect.SuccessSendBarcode,
            is Effect.ChangeSelectedDevice,
            is Effect.FailureSendBarcode,
            is Effect.StartSendBarcode,
            is Effect.SuccessChoiceMultipleDevice -> this
        }
    }

    private operator fun State.FailureInitialize.plus(effect: Effect): State {
        return when (effect) {
            Effect.StartInitialization -> State.ProcessInitialize
            Effect.CancelChoiceMultipleDevice,
            Effect.ResetSelectedDevice,
            Effect.StartChoiceMultipleDevice,
            Effect.SuccessSendBarcode,
            is Effect.ChangeSelectedDevice,
            is Effect.FailureInitialization,
            is Effect.FailureSendBarcode,
            is Effect.StartSendBarcode,
            is Effect.SuccessChoiceMultipleDevice,
            is Effect.SuccessInitialization -> this
        }
    }

    private operator fun State.NotHaveSelection.plus(effect: Effect): State {
        return when (effect) {
            Effect.SuccessSendBarcode,
            Effect.ResetSelectedDevice,
            Effect.StartInitialization,
            Effect.CancelChoiceMultipleDevice,
            is Effect.StartSendBarcode,
            is Effect.FailureSendBarcode,
            is Effect.SuccessChoiceMultipleDevice,
            is Effect.FailureInitialization,
            is Effect.SuccessInitialization -> this
            is Effect.ChangeSelectedDevice -> State.HaveSelection(barcodeReceiverKey, effect.device)
            Effect.StartChoiceMultipleDevice -> State.WaitingChoiceMultipleDevice(barcodeReceiverKey)
        }
    }

    private operator fun State.HaveSelection.plus(effect: Effect): State {
        return when (effect) {
            Effect.ResetSelectedDevice -> State.NotHaveSelection(barcodeReceiverKey)
            Effect.StartChoiceMultipleDevice -> State.WaitingChoiceMultipleDevice(barcodeReceiverKey)
            is Effect.StartSendBarcode -> State.ProcessSendBarcode(
                device = this.device,
                barcodeSending = effect.barcode,
                barcodeReceiverKey = barcodeReceiverKey
            )
            is Effect.ChangeSelectedDevice -> State.HaveSelection(barcodeReceiverKey, effect.device)
            Effect.SuccessSendBarcode,
            Effect.CancelChoiceMultipleDevice,
            Effect.StartInitialization,
            is Effect.FailureSendBarcode,
            is Effect.FailureInitialization,
            is Effect.SuccessInitialization,
            is Effect.SuccessChoiceMultipleDevice -> this
        }
    }

    private operator fun State.WaitingChoiceMultipleDevice.plus(effect: Effect): State {
        return when (effect) {
            Effect.CancelChoiceMultipleDevice -> State.NotHaveSelection(barcodeReceiverKey)
            is Effect.ChangeSelectedDevice -> State.HaveSelection(barcodeReceiverKey, effect.device)
            is Effect.SuccessChoiceMultipleDevice -> State.HaveMultipleChoiceDevices(barcodeReceiverKey, effect.devices)
            Effect.ResetSelectedDevice,
            Effect.StartChoiceMultipleDevice,
            Effect.SuccessSendBarcode,
            Effect.StartInitialization,
            is Effect.StartSendBarcode,
            is Effect.FailureInitialization,
            is Effect.SuccessInitialization,
            is Effect.FailureSendBarcode -> this
        }
    }

    private operator fun State.HaveMultipleChoiceDevices.plus(effect: Effect): State {
        return when (effect) {
            Effect.ResetSelectedDevice -> State.NotHaveSelection(barcodeReceiverKey)
            Effect.StartChoiceMultipleDevice -> State.WaitingChoiceMultipleDevice(barcodeReceiverKey)
            is Effect.ChangeSelectedDevice -> State.HaveSelection(barcodeReceiverKey, effect.device)
            is Effect.StartSendBarcode -> State.ProcessSendBarcodeMultiple(
                devices = this.devices,
                barcodeSending = effect.barcode,
                barcodeReceiverKey = barcodeReceiverKey
            )
            Effect.CancelChoiceMultipleDevice,
            Effect.SuccessSendBarcode,
            Effect.StartInitialization,
            is Effect.SuccessChoiceMultipleDevice,
            is Effect.FailureInitialization,
            is Effect.SuccessInitialization,
            is Effect.FailureSendBarcode -> this
        }
    }

    private operator fun State.ProcessSendBarcode.plus(effect: Effect): State {
        return when (effect) {
            Effect.SuccessSendBarcode -> State.HaveSelection(barcodeReceiverKey, this.device)
            is Effect.FailureSendBarcode -> State.HaveSelection(barcodeReceiverKey, this.device)
            Effect.ResetSelectedDevice,
            Effect.CancelChoiceMultipleDevice,
            Effect.StartChoiceMultipleDevice,
            Effect.StartInitialization,
            is Effect.ChangeSelectedDevice,
            is Effect.StartSendBarcode,
            is Effect.FailureInitialization,
            is Effect.SuccessInitialization,
            is Effect.SuccessChoiceMultipleDevice -> this
        }
    }

    private operator fun State.ProcessSendBarcodeMultiple.plus(effect: Effect): State {
        return when (effect) {
            Effect.SuccessSendBarcode -> State.HaveMultipleChoiceDevices(barcodeReceiverKey, this.devices)
            is Effect.FailureSendBarcode -> State.HaveMultipleChoiceDevices(barcodeReceiverKey, this.devices)
            Effect.CancelChoiceMultipleDevice,
            Effect.ResetSelectedDevice,
            Effect.StartChoiceMultipleDevice,
            Effect.StartInitialization,
            is Effect.ChangeSelectedDevice,
            is Effect.StartSendBarcode,
            is Effect.FailureInitialization,
            is Effect.SuccessInitialization,
            is Effect.SuccessChoiceMultipleDevice -> this
        }
    }
}