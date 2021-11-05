package com.project.iosephknecht.barcode_sender_plugin.data

/**
 * Enum for barcode type.
 *
 * @param title value for display name.
 *
 * @author IosephKnecht.
 */
internal enum class BarcodeType(val title: String) {
    /**
     * EAN8 barcode type.
     */
    EAN8("EAN8"),

    /**
     * EAN13 barcode type.
     */
    EAN13("EAN13"),

    /**
     * EAN13 + 2 barcode type.
     */
    EAN132("EAN13 + 2"),
    /**
     * EAN13 + 5 barcode type.
     */
    EAN135("EAN13 + 5"),

    /**
     * GTIN14 barcode tyoe.
     */
    GTIN("GTIN"),

    /**
     * UPC version A barcode type.
     */
    UPC("UPC");
}