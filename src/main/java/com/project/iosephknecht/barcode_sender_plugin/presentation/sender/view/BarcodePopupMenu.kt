package com.project.iosephknecht.barcode_sender_plugin.presentation.sender.view

import com.intellij.openapi.ui.JBMenuItem
import com.intellij.openapi.ui.JBPopupMenu
import com.project.iosephknecht.barcode_sender_plugin.presentation.sender.component.BarcodeComponent
import java.awt.event.ActionListener

/**
 * Popup menu for barcode type list.
 *
 * @param list list of barcode types.
 * @param actionListener listener for action of barcode type elements.
 *
 * @author IosephKnecht
 */
internal class BarcodePopupMenu(
    list: List<BarcodeComponent.BarcodeGenerateItem>,
    actionListener: ActionListener
) : JBPopupMenu() {

    init {
        list
            .asSequence()
            .map {
                JBMenuItem(it.title).apply {
                    actionCommand = it.actionCode
                    addActionListener(actionListener)
                }
            }
            .forEach(::add)
    }
}