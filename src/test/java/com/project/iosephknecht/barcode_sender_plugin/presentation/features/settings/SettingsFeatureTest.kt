package com.project.iosephknecht.barcode_sender_plugin.presentation.features.settings

import com.project.iosephknecht.barcode_sender_plugin.domain.FakeSettingsStorage
import com.project.iosephknecht.barcode_sender_plugin.domain.SettingsStorage
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.SchedulersContainer
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.TestTrampoline
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.states
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.settings.SettingsFeatureContract.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

internal class SettingsFeatureTest {

    @Test
    fun initialize_feature_when_state_not_initialize() {
        val feature = createFeature(State.NotInitialize)
        val observer = feature.states.test()

        observer.assertValueCount(1)
        observer.assertValueAt(0) { it is State.NotInitialize }

        feature.accept(Intent.LoadSettings)

        observer.assertValueCount(3)
        observer.assertValueAt(1) { it is State.ProcessInitialize }
        observer.assertValueAt(2) { it is State.Data }
        observer.assertValueAt(2) {
            it as State.Data
            it.barcodeReceiverKey == BARCODE_RECEIVER_KEY
        }
    }

    @Test
    fun save_settings_when_state_not_initialize() {
        val initialState = State.NotInitialize
        val feature = createFeature(initialState)
        val observer = feature.states.test()

        observer.assertValueCount(1)
        observer.assertValueAt(0, initialState)

        feature.accept(Intent.SaveSettings)

        observer.assertValueCount(1)
        observer.assertValueAt(0) { initialState === it }
    }

    @Test
    fun reset_settings_when_state_not_initialize() {
        val initialState = State.NotInitialize
        val feature = createFeature(initialState)
        val observer = feature.states.test()

        observer.assertValueCount(1)
        observer.assertValueAt(0, initialState)

        feature.accept(Intent.ResetSettings)

        observer.assertValueCount(1)
        observer.assertValueAt(0) { initialState === it }
    }

    @Test
    fun set_barcode_receiver_key_when_state_not_initialize() {
        val initialState = State.NotInitialize
        val feature = createFeature(initialState)
        val observer = feature.states.test()

        observer.assertValueCount(1)
        observer.assertValueAt(0, initialState)

        feature.accept(Intent.SetBarcodeReceiveKey(CHANGED_BARCODE_RECEIVER_KEY))

        observer.assertValueCount(1)
        observer.assertValueAt(0) { initialState === it }
    }

    @Test
    fun set_barcode_receiver_key_when_state_data() {
        val initialState = State.Data(barcodeReceiverKey = BARCODE_RECEIVER_KEY)
        val feature = createFeature(initialState)

        val testObserver = feature.states.test()

        testObserver.assertValueCount(1)
        testObserver.assertValueAt(0) { initialState === it }
        testObserver.assertValueAt(0, initialState)

        feature.accept(Intent.SetBarcodeReceiveKey(CHANGED_BARCODE_RECEIVER_KEY))

        testObserver.assertValueCount(2)
        testObserver.assertValueAt(1) { it is State.ChangedSettings }
        testObserver.assertValueAt(1) {
            it as State.ChangedSettings
            it.originalBarcodeReceiverKey == BARCODE_RECEIVER_KEY &&
                it.changedBarcodeReceiverKey == CHANGED_BARCODE_RECEIVER_KEY
        }
    }

    @Test
    fun set_not_changed_barcode_receiver_key_when_state_data() {
        val initialState = State.Data(barcodeReceiverKey = BARCODE_RECEIVER_KEY)
        val feature = createFeature(initialState)

        val testObserver = feature.states.test()

        testObserver.assertValueCount(1)
        testObserver.assertValueAt(0) { initialState === it }
        testObserver.assertValueAt(0, initialState)

        feature.accept(Intent.SetBarcodeReceiveKey(BARCODE_RECEIVER_KEY))

        testObserver.assertValueCount(2)
        testObserver.assertValueAt(1) { initialState === it }
        testObserver.assertValueAt(1, initialState)
    }

    private fun createFeature(initialState: State): Feature {
        return SettingsFeatureFactoryFake(
            settingsStorage = settingsStorage,
            initialState = initialState,
            schedulersContainer = SchedulersContainer.TestTrampoline()
        ).create()
    }

    companion object {
        private const val BARCODE_RECEIVER_KEY = "TEST_BARCODE_RECEIVER_KEY"
        private const val CHANGED_BARCODE_RECEIVER_KEY = "CHANGED_BARCODE_RECEIVER_KEY"

        lateinit var settingsStorage: SettingsStorage

        @BeforeAll
        @JvmStatic
        internal fun beforeAll() {
            settingsStorage = FakeSettingsStorage(BARCODE_RECEIVER_KEY)
        }
    }
}