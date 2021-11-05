package com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_generator_multiple

import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_generator_multiple.BarcodeGeneratorMultipleFeatureContract.ActionMapper
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_generator_multiple.BarcodeGeneratorMultipleFeatureContract.State
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_generator_multiple.BarcodeGeneratorMultipleFeatureContract.Intent
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_generator_multiple.BarcodeGeneratorMultipleFeatureContract.Action

/**
 * Default implementation [ActionMapper].
 *
 * @author IosephKnecht
 */
internal class BarcodeGeneratorMultipleActionMapperDefault : ActionMapper {

    override fun map(
        state: State,
        intent: Intent
    ): Action? {
        return when (state) {
            is State.Empty,
            is State.Data -> when (intent) {
                Intent.GenerateBarcodesList -> Action.ExecuteGenerateBarcodeList
                is Intent.SetBarcodeCount -> Action.ExecuteSetBarcodeCount(intent.barcodeCount)
                is Intent.SetBarcodeType -> Action.ExecuteSetBarcodeType(intent.barcodeType)
                Intent.ApproveBarcodeList -> Action.ExecuteApproveList
            }
            is State.ProcessGenerating -> when (intent) {
                Intent.ApproveBarcodeList -> Action.ExecuteApproveList
                Intent.GenerateBarcodesList,
                is Intent.SetBarcodeCount,
                is Intent.SetBarcodeType -> null
            }
        }
    }
}