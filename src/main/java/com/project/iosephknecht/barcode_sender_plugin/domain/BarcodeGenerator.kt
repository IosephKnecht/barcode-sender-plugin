package com.project.iosephknecht.barcode_sender_plugin.domain

import com.project.iosephknecht.barcode_sender_plugin.data.BarcodeType

/**
 * Contract for barcode generator.
 *
 * @author IosephKnecht
 */
internal interface BarcodeGenerator {

    /**
     * Generate barcode by type.
     *
     * @param barcodeType type of barcode.
     *
     * @return barcode
     */
    fun generate(barcodeType: BarcodeType): String
}

/**
 * Default implementation for [BarcodeGenerator].
 *
 * @author IosephKnecht
 */
internal class DefaultBarcodeGenerator : BarcodeGenerator {

    override fun generate(barcodeType: BarcodeType): String {
        return when (barcodeType) {
            BarcodeType.EAN8 -> ean8()
            BarcodeType.EAN13 -> ean13()
            BarcodeType.EAN132 -> ean132()
            BarcodeType.EAN135 -> ean135()
            BarcodeType.GTIN -> gtin14()
            BarcodeType.UPC -> upcA()
        }
    }

    private fun ean8(): String = generateBarcodeSequence()
        .take(7)
        .plusControlDigit()
        .joinToBarcode()

    private fun ean13(): String = generateBarcodeSequence()
        .take(12)
        .plusControlDigit()
        .joinToBarcode()

    private fun ean132(): String = generateBarcodeSequence()
        .take(12)
        .plusControlDigit()
        .plus(generateBarcodeSequence { random.nextInt(10) }.take(2).toList())
        .joinToBarcode()

    private fun ean135(): String = generateBarcodeSequence()
        .take(12)
        .plusControlDigit()
        .plus(generateBarcodeSequence { nextInt(10) }.take(5).toList())
        .joinToBarcode()

    private fun gtin14(): String = generateBarcodeSequence()
        .take(13)
        .plusControlDigit()
        .joinToBarcode()

    private fun upcA(): String = generateBarcodeSequence()
        .take(11)
        .plusControlDigit()
        .joinToBarcode()

    private companion object {

        val random = java.util.Random()

        fun generateBarcodeSequence(
            seed: java.util.Random.() -> Int = { nextInt(9) + 1 }
        ) = generateSequence(
            seedFunction = { seed.invoke(random) },
            nextFunction = { random.nextInt(10) }
        )

        fun Sequence<Int>.plusControlDigit(): List<Int> {
            var index = -1
            var oddSum = 0
            var evenSum = 0

            val list = mutableListOf<Int>()

            for (value in this) {
                index++
                list.add(value)
                val newIndex = index + 1

                when (newIndex % 2) {
                    0 -> evenSum += value
                    else -> oddSum += value
                }
            }

            val size = index + 1

            when (size % 2) {
                0 -> evenSum *= 3
                else -> oddSum *= 3
            }

            val controlDigit = ((10 - ((oddSum + evenSum) % 10)) % 10)

            return list.plus(controlDigit)
        }

        fun List<Int>.joinToBarcode(): String {
            val buffer = StringBuilder()

            for (digit in this) buffer.append(digit)

            return buffer.toString()
        }
    }
}