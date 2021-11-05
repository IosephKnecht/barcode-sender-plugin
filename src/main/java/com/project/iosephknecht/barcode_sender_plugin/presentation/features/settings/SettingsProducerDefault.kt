package com.project.iosephknecht.barcode_sender_plugin.presentation.features.settings

import com.project.iosephknecht.barcode_sender_plugin.domain.SettingsStorage
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.ConfigurableStateOwner
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.DefaultConfigurableStateOwner
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.settings.SettingsFeatureContract.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

/**
 * Default implementation [Producer].
 *
 * @param settingsStorage
 *
 * @author IosephKnecht
 */
internal class SettingsProducerDefault(
    private val settingsStorage: SettingsStorage
) : Producer,
    ConfigurableStateOwner<State> by DefaultConfigurableStateOwner() {

    override fun produce(action: Action): Observable<Effect> {
        return when (action) {
            Action.ExecuteLoadSettings -> loadSettings()
            Action.ExecuteResetSettings -> resetSettings()
            Action.ExecuteSaveSettings -> saveSetting(state)
            is Action.ExecuteSetBarcodeReceiveKey -> setBarcodeReceiverKey(state, action.key)
        }
    }

    private fun loadSettings(): Observable<Effect> {
        return Observable.fromSingle(Single.fromCallable { settingsStorage.getBarcodeReceiverKey().orEmpty() })
            .map<Effect>(Effect::SuccessLoadSettings)
            .onErrorReturn(Effect::FailureLoadSettings)
            .startWithItem(Effect.StartLoadSettings)
    }

    private fun resetSettings(): Observable<Effect> {
        return Observable.just(Effect.ResetSettings)
    }

    private fun saveSetting(state: State): Observable<Effect> {
        val changedReceiverKey = when (state) {
            State.NotInitialize,
            State.ProcessInitialize,
            is State.FailureInitialize,
            is State.Data,
            is State.ProcessSaveSettings -> return Observable.empty()
            is State.ChangedSettings -> state.changedBarcodeReceiverKey
        }

        return Observable.fromCallable { settingsStorage.setBarcodeReceiverKey(changedReceiverKey) }
            .map<Effect> { Effect.SuccessSaveSettings(changedReceiverKey) }
            .onErrorReturn(Effect::FailureSaveSettings)
            .startWithItem(Effect.StartSaveSettings)
    }

    private fun setBarcodeReceiverKey(state: State, receiverKey: String): Observable<Effect> {
        val previousReceiverKey = when (state) {
            State.NotInitialize,
            State.ProcessInitialize,
            is State.FailureInitialize,
            is State.ProcessSaveSettings -> return Observable.empty()
            is State.Data -> state.barcodeReceiverKey
            is State.ChangedSettings -> when (state.changedBarcodeReceiverKey) {
                receiverKey -> return Observable.empty()
                else -> state.originalBarcodeReceiverKey
            }
        }

        if (previousReceiverKey == receiverKey) return Observable.just(Effect.ResetSettings)

        return Observable.just(receiverKey)
            .map(Effect::SetBarcodeReceiveKey)
    }
}