package com.project.iosephknecht.barcode_sender_plugin.presentation.multiple_choice.component

import com.android.ddmlib.IDevice

/**
 * Contract for listener multiple choice devices.
 *
 * @author IosephKnecht
 */
internal interface MultipleChoiceListener {

    /**
     * Selection was completed successfully.
     */
    fun successChoice(devices: Set<IDevice>)

    /**
     * Selection was completed failed.
     */
    fun interruptChoice()
}