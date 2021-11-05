package com.project.iosephknecht.barcode_sender_plugin.presentation.multiple_choice

import com.intellij.ui.components.JBCheckBox
import com.project.iosephknecht.barcode_sender_plugin.presentation.utils.deviceName
import java.awt.Component
import javax.swing.JList
import javax.swing.ListCellRenderer
import com.project.iosephknecht.barcode_sender_plugin.presentation.multiple_choice.component.MultipleChoiceComponent.Device as Item

/**
 * Implementation [ListCellRenderer] for render component of [JBCheckBox].
 *
 * @author IosephKnecht
 */
internal class MultipleChoiceCellRenderer : JBCheckBox(), ListCellRenderer<Item> {

    override fun getListCellRendererComponent(
        list: JList<out Item>,
        value: Item,
        index: Int,
        isSelected: Boolean,
        cellHasFocus: Boolean
    ): Component {
        componentOrientation = list.componentOrientation
        font = list.font
        background = list.background
        foreground = list.foreground
        isEnabled = list.isEnabled
        this.isSelected = value.isSelected
        text = value.device.deviceName

        return this
    }
}