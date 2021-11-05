package com.project.iosephknecht.barcode_sender_plugin.presentation.features.recent_barcode_type

import com.project.iosephknecht.barcode_sender_plugin.presentation.features.recent_barcode_type.RecentBarcodeTypeFeatureContract.ActionMapper
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.recent_barcode_type.RecentBarcodeTypeFeatureContract.State
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.recent_barcode_type.RecentBarcodeTypeFeatureContract.Intent
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.recent_barcode_type.RecentBarcodeTypeFeatureContract.Action

/**
 * Default implementation [ActionMapper].
 *
 * @author IosephKnecht
 */
internal class RecentBarcodeTypeActionMapperDefault : ActionMapper {

    override fun map(
        state: State,
        intent: Intent
    ): Action? {
        return when (state) {
            State.NotInitialized -> when (intent) {
                Intent.Initialize -> Action.ExecuteInitialize
                is Intent.ChoiceBarcodeType -> null
            }
            State.ProcessInitialized -> when (intent) {
                Intent.Initialize,
                is Intent.ChoiceBarcodeType -> null
            }
            is State.Data -> when (intent) {
                is Intent.ChoiceBarcodeType -> Action.ExecuteChoiceBarcodeType(intent.barcodeType)
                Intent.Initialize -> null
            }
            is State.ProcessChangeRecent -> when (intent) {
                Intent.Initialize,
                is Intent.ChoiceBarcodeType -> null
            }
        }
    }
}