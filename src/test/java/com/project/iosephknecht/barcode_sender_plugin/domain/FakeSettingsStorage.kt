package com.project.iosephknecht.barcode_sender_plugin.domain

internal class FakeSettingsStorage(
    barcodeReceiverKey: String?
) : SettingsStorage {

    @Suppress("CanBePrimaryConstructorProperty")
    private var barcodeReceiverKey = barcodeReceiverKey

    override fun getBarcodeReceiverKey(): String? {
        return barcodeReceiverKey
    }

    override fun setBarcodeReceiverKey(receiverKey: String) {
        barcodeReceiverKey = receiverKey
    }
}