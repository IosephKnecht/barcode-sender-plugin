package com.project.iosephknecht.barcode_sender_plugin.presentation.multiple_generator.view

import com.project.iosephknecht.barcode_sender_plugin.data.BarcodeType

/**
 * Listener contract for the multi-barcode generation dialog.
 *
 * @author IosephKnecht
 */
internal interface MultipleGeneratorListener {

    /**
     * Callback after approve barcode list.
     *
     * @param barcodeType type of barcodes.
     * @param list list of barcodes.
     */
    fun approve(barcodeType: BarcodeType, list: List<String>)

    /**
     * Callback after cancel barcode list.
     */
    fun cancel()
}