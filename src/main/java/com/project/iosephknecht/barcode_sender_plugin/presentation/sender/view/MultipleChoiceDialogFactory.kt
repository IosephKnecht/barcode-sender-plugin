package com.project.iosephknecht.barcode_sender_plugin.presentation.sender.view

import com.intellij.openapi.ui.DialogWrapper
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.devices.DevicesFeatureContract
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.devices_selector.DevicesSelectorFeatureContract
import com.project.iosephknecht.barcode_sender_plugin.presentation.multiple_choice.MultipleChoiceDevicesDialog
import com.project.iosephknecht.barcode_sender_plugin.presentation.multiple_choice.component.MultipleChoiceListener

/**
 * Factory contract for multiple choice dialog.
 *
 * @author IosephKnecht
 */
internal interface MultipleChoiceDialogFactory {

    /**
     * Create dialog.
     */
    fun create(): DialogWrapper
}

/**
 * Default implementation [MultipleChoiceDialogFactory].
 *
 * @param devicesSelectorFeature
 * @param devicesFeature
 * @param listener
 *
 * @author IosephKnecht
 */
internal class MultipleChoiceDialogFactoryDefault(
    private val devicesSelectorFeature: DevicesSelectorFeatureContract.Feature,
    private val devicesFeature: DevicesFeatureContract.Feature,
    private val listener: MultipleChoiceListener
) : MultipleChoiceDialogFactory {

    override fun create(): DialogWrapper {
        return MultipleChoiceDevicesDialog(
            devicesFeature = devicesFeature,
            devicesSelector = devicesSelectorFeature,
            listener = listener
        )
    }
}