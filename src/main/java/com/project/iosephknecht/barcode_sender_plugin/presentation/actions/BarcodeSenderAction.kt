package com.project.iosephknecht.barcode_sender_plugin.presentation.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.project.iosephknecht.barcode_sender_plugin.presentation.sender.view.BarcodeSenderDialog

/**
 * Action for call BarcodeSenderDialog.
 *
 * @author IosephKnecht
 */
internal class BarcodeSenderAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        e.project?.let { BarcodeSenderDialog(it).show() }
    }
}