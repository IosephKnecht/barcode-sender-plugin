package com.project.iosephknecht.barcode_sender_plugin.domain

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil
import com.project.iosephknecht.barcode_sender_plugin.data.BarcodeType
import javax.swing.SwingUtilities

/**
 * Contract for local storage.
 *
 * @author IosephKnecht
 */
internal interface LocalStorage {

    /**
     * Save latest barcode - text.
     *
     * @param value text
     */
    fun setLatestCode(value: String)

    /**
     * Get latest barcode - text.
     */
    fun getLatestCode(): String?

    /**
     * Set recent barcode type map where key - type of barcode,
     * value - last timestamp use type of barcode.
     */
    fun setRecentBarcodeType(map: Map<BarcodeType, Long>)

    /**
     * Get recent map barcode type map where key - type of barcode,
     * value - last timestamp use type of barcode.
     */
    fun getRecentBarcodeType(): Map<BarcodeType, Long>?
}

/**
 * Default implementation for [LocalStorage].
 *
 * @see [PersistentStateComponent]
 *
 * @author IosephKnecht
 */
@com.intellij.openapi.components.State(
    name = "com.project.iosephknecht.barcode_sender_plugin.local_storage.state",
    storages = [Storage("com.project.iosephknecht.barcode_sender_plugin.local_storage")]
)
internal class DefaultLocalStorage : PersistentStateComponent<DefaultLocalStorage.State>, LocalStorage {

    private val state = State()

    override fun getState(): State {
        return state
    }

    override fun loadState(state: State) {
        XmlSerializerUtil.copyBean(state, this.state)
    }

    override fun setLatestCode(value: String) {
        ensureEventDispatchThread { state.latestCode = value }
    }

    override fun getLatestCode(): String? {
        return ensureEventDispatchThread { state.latestCode }
    }

    override fun setRecentBarcodeType(map: Map<BarcodeType, Long>) {
        ensureEventDispatchThread { state.recentBarcodeType = map }
    }

    override fun getRecentBarcodeType(): Map<BarcodeType, Long>? {
        return ensureEventDispatchThread { state.recentBarcodeType }
    }

    private fun <T> ensureEventDispatchThread(block: () -> T): T {
        require(SwingUtilities.isEventDispatchThread()) { "operation must be execute on event dispatch thread" }
        return block.invoke()
    }

    class State(
        var latestCode: String? = null,
        var recentBarcodeType: Map<BarcodeType, Long>? = null
    )
}