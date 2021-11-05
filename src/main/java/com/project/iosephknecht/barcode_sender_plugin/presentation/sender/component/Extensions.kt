package com.project.iosephknecht.barcode_sender_plugin.presentation.sender.component

import com.android.ddmlib.IDevice
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.device_selector.DeviceSelectorFeatureContract
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.devices.DevicesFeatureContract
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_editor.BarcodeEditorFeatureContract

// region DevicesFeatureContract.State
internal inline val DevicesFeatureContract.State.isInitialized: Boolean
    get() = when (this) {
        DevicesFeatureContract.State.UnInitialize -> false
        DevicesFeatureContract.State.ProcessInitialize -> false
        is DevicesFeatureContract.State.FailureInitialize -> false
        is DevicesFeatureContract.State.Data -> true
        is DevicesFeatureContract.State.ProcessLoadDevices -> true
    }

internal inline val DevicesFeatureContract.State.items: List<IDevice>?
    get() = when (this) {
        is DevicesFeatureContract.State.Data -> this.devices
        is DevicesFeatureContract.State.FailureInitialize -> null
        DevicesFeatureContract.State.ProcessInitialize -> null
        is DevicesFeatureContract.State.ProcessLoadDevices -> this.devices
        DevicesFeatureContract.State.UnInitialize -> null
    }

internal inline val DevicesFeatureContract.State.isProcessInitialize: Boolean
    get() = when (this) {
        DevicesFeatureContract.State.ProcessInitialize -> true
        is DevicesFeatureContract.State.Data,
        is DevicesFeatureContract.State.FailureInitialize,
        is DevicesFeatureContract.State.ProcessLoadDevices,
        DevicesFeatureContract.State.UnInitialize -> false
    }

internal inline val DevicesFeatureContract.State.isInitializedError: Boolean
    get() = when (this) {
        is DevicesFeatureContract.State.FailureInitialize -> true
        is DevicesFeatureContract.State.Data,
        DevicesFeatureContract.State.ProcessInitialize,
        is DevicesFeatureContract.State.ProcessLoadDevices,
        DevicesFeatureContract.State.UnInitialize -> false
    }
// endregion

// region DeviceSelectorFeatureContract.State
internal val DeviceSelectorFeatureContract.State.isInitialized: Boolean
    get() = when (this) {
        DeviceSelectorFeatureContract.State.NotInitialized,
        DeviceSelectorFeatureContract.State.ProcessInitialize,
        is DeviceSelectorFeatureContract.State.FailureInitialize -> false
        is DeviceSelectorFeatureContract.State.HaveMultipleChoiceDevices,
        is DeviceSelectorFeatureContract.State.HaveSelection,
        is DeviceSelectorFeatureContract.State.NotHaveSelection,
        is DeviceSelectorFeatureContract.State.ProcessSendBarcode,
        is DeviceSelectorFeatureContract.State.ProcessSendBarcodeMultiple,
        is DeviceSelectorFeatureContract.State.WaitingChoiceMultipleDevice -> true
    }

internal inline val DeviceSelectorFeatureContract.State.selectedItem: IDevice?
    get() = when (this) {
        is DeviceSelectorFeatureContract.State.HaveSelection -> this.device
        is DeviceSelectorFeatureContract.State.ProcessSendBarcode -> this.device
        is DeviceSelectorFeatureContract.State.NotHaveSelection,
        DeviceSelectorFeatureContract.State.NotInitialized,
        DeviceSelectorFeatureContract.State.ProcessInitialize,
        is DeviceSelectorFeatureContract.State.FailureInitialize,
        is DeviceSelectorFeatureContract.State.WaitingChoiceMultipleDevice,
        is DeviceSelectorFeatureContract.State.HaveMultipleChoiceDevices,
        is DeviceSelectorFeatureContract.State.ProcessSendBarcodeMultiple -> null
    }

internal inline val DeviceSelectorFeatureContract.State.isAvailableSendBarcode: Boolean
    get() = when (this) {
        is DeviceSelectorFeatureContract.State.HaveSelection,
        is DeviceSelectorFeatureContract.State.HaveMultipleChoiceDevices -> true
        DeviceSelectorFeatureContract.State.NotInitialized,
        DeviceSelectorFeatureContract.State.ProcessInitialize,
        is DeviceSelectorFeatureContract.State.FailureInitialize,
        is DeviceSelectorFeatureContract.State.ProcessSendBarcode,
        is DeviceSelectorFeatureContract.State.ProcessSendBarcodeMultiple,
        is DeviceSelectorFeatureContract.State.WaitingChoiceMultipleDevice,
        is DeviceSelectorFeatureContract.State.NotHaveSelection -> false
    }

internal val DeviceSelectorFeatureContract.State.isInitializedError: Boolean
    get() = when (this) {
        is DeviceSelectorFeatureContract.State.FailureInitialize -> true
        DeviceSelectorFeatureContract.State.NotInitialized,
        DeviceSelectorFeatureContract.State.ProcessInitialize,
        is DeviceSelectorFeatureContract.State.HaveMultipleChoiceDevices,
        is DeviceSelectorFeatureContract.State.HaveSelection,
        is DeviceSelectorFeatureContract.State.NotHaveSelection,
        is DeviceSelectorFeatureContract.State.ProcessSendBarcode,
        is DeviceSelectorFeatureContract.State.ProcessSendBarcodeMultiple,
        is DeviceSelectorFeatureContract.State.WaitingChoiceMultipleDevice -> false
    }

internal val DeviceSelectorFeatureContract.State.isProcessInitialize: Boolean
    get() = when (this) {
        DeviceSelectorFeatureContract.State.ProcessInitialize -> true
        DeviceSelectorFeatureContract.State.NotInitialized,
        is DeviceSelectorFeatureContract.State.FailureInitialize,
        is DeviceSelectorFeatureContract.State.HaveMultipleChoiceDevices,
        is DeviceSelectorFeatureContract.State.HaveSelection,
        is DeviceSelectorFeatureContract.State.NotHaveSelection,
        is DeviceSelectorFeatureContract.State.ProcessSendBarcode,
        is DeviceSelectorFeatureContract.State.ProcessSendBarcodeMultiple,
        is DeviceSelectorFeatureContract.State.WaitingChoiceMultipleDevice -> false
    }

internal val DeviceSelectorFeatureContract.State.errorReason: DeviceSelectorFeatureContract.ErrorReason?
    get() = when (this) {
        is DeviceSelectorFeatureContract.State.FailureInitialize -> this.reason
        DeviceSelectorFeatureContract.State.NotInitialized,
        DeviceSelectorFeatureContract.State.ProcessInitialize,
        is DeviceSelectorFeatureContract.State.HaveMultipleChoiceDevices,
        is DeviceSelectorFeatureContract.State.HaveSelection,
        is DeviceSelectorFeatureContract.State.NotHaveSelection,
        is DeviceSelectorFeatureContract.State.ProcessSendBarcode,
        is DeviceSelectorFeatureContract.State.ProcessSendBarcodeMultiple,
        is DeviceSelectorFeatureContract.State.WaitingChoiceMultipleDevice -> null
    }
// endregion

// region BarcodeEditorFeatureContract.State
internal val BarcodeEditorFeatureContract.State.isInitialized: Boolean
    get() = when (this) {
        BarcodeEditorFeatureContract.State.NotInitialized -> false
        is BarcodeEditorFeatureContract.State.Initialized -> true
    }

internal inline val BarcodeEditorFeatureContract.State.barcode: String?
    get() = when (this) {
        is BarcodeEditorFeatureContract.State.Initialized -> this.barcodeText
        BarcodeEditorFeatureContract.State.NotInitialized -> null
    }
// endregion