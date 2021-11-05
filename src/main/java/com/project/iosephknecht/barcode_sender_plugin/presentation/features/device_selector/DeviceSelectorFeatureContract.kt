package com.project.iosephknecht.barcode_sender_plugin.presentation.features.device_selector

import com.android.ddmlib.IDevice
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.EffectProducer
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.StateReducer
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.Store

/**
 * Extension - value for get devices, actual for single and multiple mode.
 */
internal val DeviceSelectorFeatureContract.State.devices: Set<IDevice>?
    get() = when (this) {
        is DeviceSelectorFeatureContract.State.HaveMultipleChoiceDevices -> this.devices
        is DeviceSelectorFeatureContract.State.ProcessSendBarcodeMultiple -> this.devices
        is DeviceSelectorFeatureContract.State.HaveSelection -> setOf(this.device)
        is DeviceSelectorFeatureContract.State.ProcessSendBarcode -> setOf(this.device)
        DeviceSelectorFeatureContract.State.NotInitialized,
        DeviceSelectorFeatureContract.State.ProcessInitialize,
        is DeviceSelectorFeatureContract.State.FailureInitialize,
        is DeviceSelectorFeatureContract.State.WaitingChoiceMultipleDevice,
        is DeviceSelectorFeatureContract.State.NotHaveSelection -> null
    }

/**
 * Extension - value for get devices from multiple choice state.
 * If state not supported multiple choice then return null.
 */
internal val DeviceSelectorFeatureContract.State.multipleChoiceDevices: Set<IDevice>?
    get() = when (this) {
        is DeviceSelectorFeatureContract.State.WaitingChoiceMultipleDevice -> emptySet()
        is DeviceSelectorFeatureContract.State.HaveMultipleChoiceDevices -> this.devices
        is DeviceSelectorFeatureContract.State.ProcessSendBarcodeMultiple -> this.devices
        DeviceSelectorFeatureContract.State.NotInitialized,
        DeviceSelectorFeatureContract.State.ProcessInitialize,
        is DeviceSelectorFeatureContract.State.FailureInitialize,
        is DeviceSelectorFeatureContract.State.NotHaveSelection,
        is DeviceSelectorFeatureContract.State.HaveSelection,
        is DeviceSelectorFeatureContract.State.ProcessSendBarcode -> null
    }

internal val DeviceSelectorFeatureContract.State.barcodeReceiverKey: String?
    get() = when (this) {
        is DeviceSelectorFeatureContract.State.NotHaveSelection -> barcodeReceiverKey
        is DeviceSelectorFeatureContract.State.HaveSelection -> barcodeReceiverKey
        is DeviceSelectorFeatureContract.State.HaveMultipleChoiceDevices -> barcodeReceiverKey
        is DeviceSelectorFeatureContract.State.WaitingChoiceMultipleDevice -> barcodeReceiverKey
        is DeviceSelectorFeatureContract.State.ProcessSendBarcode -> barcodeReceiverKey
        is DeviceSelectorFeatureContract.State.ProcessSendBarcodeMultiple -> barcodeReceiverKey
        DeviceSelectorFeatureContract.State.NotInitialized,
        DeviceSelectorFeatureContract.State.ProcessInitialize,
        is DeviceSelectorFeatureContract.State.FailureInitialize -> null
    }

/**
 * Contract for device selector feature.
 *
 * @author IosephKnecht
 */
internal interface DeviceSelectorFeatureContract {

    interface Feature : Store<State, Intent, News>

    interface Reducer : StateReducer<State, Effect>

    interface Producer : EffectProducer<State, Action, Effect>

    interface Bootstrapper : com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.Bootstrapper<State, Action>

    interface ActionMapper : com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.ActionMapper<State, Intent, Action>

    interface NewsMapper : com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.NewsMapper<State, Effect, News>

    /**
     * Enum of error reason.
     */
    enum class ErrorReason {
        UNINITIALIZED_BARCODE_RECEIVER_KEY,
        EMPTY_BARCODE_RECEIVER_KEY,
        UNKNOWN;
    }

    /**
     * Contract for state device selector feature.
     *
     * @author IosephKnecht
     */
    sealed interface State {
        /**
         * Not initialize.
         */
        object NotInitialized : State

        /**
         * Process initialization.
         */
        object ProcessInitialize : State

        /**
         * Failure initialization.
         *
         * @param throwable
         */
        data class FailureInitialize(
            val throwable: Throwable,
            val reason: ErrorReason
        ) : State

        /**
         * Feature not have selection.
         */
        data class NotHaveSelection(val barcodeReceiverKey: String) : State

        /**
         * Feature have selected device.
         *
         * @param device selected device.
         */
        data class HaveSelection(
            val barcodeReceiverKey: String,
            val device: IDevice
        ) : State

        /**
         * Feature waiting multiple choice devices.
         */
        data class WaitingChoiceMultipleDevice(val barcodeReceiverKey: String) : State

        /**
         * Feature have selection multiple devices.
         *
         * @param devices devices for send barcode.
         */
        data class HaveMultipleChoiceDevices(
            val barcodeReceiverKey: String,
            val devices: Set<IDevice>
        ) : State

        /**
         * Feature process send barcode to device.
         *
         * @param device selected device.
         * @param barcodeSending sending barcode.
         */
        data class ProcessSendBarcode(
            val barcodeReceiverKey: String,
            val device: IDevice,
            val barcodeSending: String
        ) : State

        /**
         * Feature
         */
        data class ProcessSendBarcodeMultiple(
            val barcodeReceiverKey: String,
            val devices: Set<IDevice>,
            val barcodeSending: String
        ) : State
    }

    /**
     * Contract for user wishes.
     *
     * @author IosephKnecht
     */
    sealed interface Intent {

        /**
         * Init feature state.
         */
        object Initialize : Intent

        /**
         * Intent for select device.
         *
         * @param device device for selection.
         */
        class SelectDevice(val device: IDevice) : Intent

        /**
         * Intent for reset selected device.
         */
        object ResetSelected : Intent

        /**
         * Intent for send barcode to device.
         *
         * @param value barcode.
         */
        class SendBarcode(val value: String) : Intent

        /**
         * Intent for choice multiple devices.
         */
        object ChoiceMultipleDevices : Intent

        /**
         * Intent for cancel multiple choice.
         */
        object CancelMultipleChoice : Intent

        /**
         * Intent for success cancel choice
         */
        class SuccessMultipleChoice(val devices: Set<IDevice>) : Intent
    }

    /**
     * Contract for actions.
     *
     * @author IosphKnecht
     */
    sealed interface Action {

        /**
         * Action for execute initialize.
         */
        object ExecuteInitialize : Action

        /**
         * Action for execute select device.
         *
         * @param device device will be selected.
         */
        class ExecuteSelectDevice(val device: IDevice) : Action

        /**
         * Action for execute reset selected device.
         */
        object ExecuteResetSelectDevice : Action

        /**
         * Action for execute send barcode.
         *
         * @param value barcode will be sending.
         */
        class ExecuteSendBarcode(val value: String) : Action

        /**
         * Action for execute multiple choice devices.
         */
        object ExecuteMultipleChoiceDevice : Action

        /**
         * Action for execute cancel choice multiple devices.
         */
        object ExecuteCancelMultipleChoiceDevice : Action

        /**
         * Action for execute success choice multiple devices.
         */
        class ExecuteSuccessChoiceMultipleDevice(val devices: Set<IDevice>) : Action
    }

    /**
     * Contract for effect.
     *
     * @author IosephKnecht
     */
    sealed interface Effect {

        /**
         * Effect for start initialization.
         */
        object StartInitialization : Effect

        /**
         * Effect for failure initialization.
         *
         * @param throwable
         */
        class FailureInitialization(val throwable: Throwable, val errorReason: ErrorReason) : Effect

        /**
         * Effect for success initialization.
         */
        class SuccessInitialization(val barcodeReceiverKey: String) : Effect

        /**
         * Effect for change selected device.
         *
         * @param device new selected device.
         */
        class ChangeSelectedDevice(val device: IDevice) : Effect

        /**
         * Effect for reset selected device.
         */
        object ResetSelectedDevice : Effect

        /**
         * Effect for starting choice multiple device.
         */
        object StartChoiceMultipleDevice : Effect

        /**
         * Effect for success choice multiple device.
         *
         * @param devices
         */
        class SuccessChoiceMultipleDevice(val devices: Set<IDevice>) : Effect

        /**
         * Effect for interrupt choice multiple device.
         */
        object CancelChoiceMultipleDevice : Effect

        /**
         * Effect for mark start sending barcode.
         *
         * @param barcode barcode for starting sending.
         */
        class StartSendBarcode(val barcode: String) : Effect

        /**
         * Effect for mark failure sending barcode.
         */
        class FailureSendBarcode(val throwable: Throwable) : Effect

        /**
         * Effect for mark success sending barcode.
         */
        object SuccessSendBarcode : Effect
    }

    /**
     * Contract for news.
     */
    sealed interface News {
        /**
         * News about successfully sending barcode.
         */
        object SuccessfulSendCode : News

        /**
         * News about transition to [State.WaitingChoiceMultipleDevice].
         */
        object MultipleChoiceDevice : News

        /**
         * News about failure sending barcode.
         *
         * @param throwable reason failure.
         */
        class FailureSendCode(val throwable: Throwable) : News

        /**
         * News about reset selected intent.
         */
        object ResetSelected : News

        /**
         * News about single choice device.
         *
         * @param device selected device.
         */
        class SingleChoiceDevice(val device: IDevice) : News
    }
}