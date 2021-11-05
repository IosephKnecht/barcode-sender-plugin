package com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_generator

import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_generator.BarcodeGeneratorFeatureContract.ActionMapper
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_generator.BarcodeGeneratorFeatureContract.State
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_generator.BarcodeGeneratorFeatureContract.Intent
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_generator.BarcodeGeneratorFeatureContract.Action

/**
 * Default implementation [ActionMapper].
 *
 * @author IosephKnecht
 */
internal class BarcodeGeneratorActionMapperDefault : ActionMapper {

    override fun map(
        state: State,
        intent: Intent
    ): Action? {
        return when (state) {
            State.Empty -> when (intent) {
                is Intent.GenerateBarcode -> Action.ExecuteGenerateBarcodeType(intent.barcodeType)
            }
            is State.ProcessGenerateBarcode -> when (intent) {
                is Intent.GenerateBarcode -> null
            }
        }
    }
}