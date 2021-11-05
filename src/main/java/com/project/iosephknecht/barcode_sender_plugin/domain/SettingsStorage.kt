package com.project.iosephknecht.barcode_sender_plugin.domain

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

/**
 * Contract for plugin settings.
 *
 * @author IosephKnecht
 */
internal interface SettingsStorage {

    /**
     * Get barcode receiver key.
     */
    fun getBarcodeReceiverKey(): String?

    /**
     * Set barcode receiver key.
     *
     * @param receiverKey key for send.
     */
    fun setBarcodeReceiverKey(receiverKey: String)
}

/**
 * Default implementation [SettingsStorage].
 *
 * @author IosephKnecht
 */
@State(
    name = "com.project.iosephknecht.barcode_sender_plugin.settings_state",
    storages = [Storage("com.project.iosephknecht.barcode_sender_plugin.settings")]
)
internal class DefaultSettingsStorage : SettingsStorage, PersistentStateComponent<DefaultSettingsStorage.State> {

    private val state: State = State()

    override fun getBarcodeReceiverKey(): String? = synchronized(state) { state.barcodeReceiveKey }

    override fun setBarcodeReceiverKey(receiverKey: String) {
        synchronized(state) {
            state.barcodeReceiveKey = receiverKey
        }
    }

    override fun getState(): State {
        return synchronized(state) { state.deepCopy() }
    }

    override fun loadState(state: State) {
        synchronized(this.state) {
            XmlSerializerUtil.copyBean(state, this.state)
        }
    }

    class State(var barcodeReceiveKey: String? = null)

    private companion object {

        fun State.deepCopy(): State {
            return State(barcodeReceiveKey = this.barcodeReceiveKey)
        }
    }
}