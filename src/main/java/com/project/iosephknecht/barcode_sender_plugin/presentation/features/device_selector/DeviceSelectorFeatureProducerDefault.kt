package com.project.iosephknecht.barcode_sender_plugin.presentation.features.device_selector

import com.android.ddmlib.IDevice
import com.android.ddmlib.NullOutputReceiver
import com.project.iosephknecht.barcode_sender_plugin.domain.SettingsStorage
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.ConfigurableStateOwner
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.DefaultConfigurableStateOwner
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.SchedulersContainer
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.device_selector.DeviceSelectorFeatureContract.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

/**
 * Implementation effect producer for device selector feature.
 *
 * @param settingsStorage
 *
 * @author IosephKnecht
 */
internal class DeviceSelectorFeatureProducerDefault(
    private val settingsStorage: SettingsStorage,
    private val schedulersContainer: SchedulersContainer
) : Producer,
    ConfigurableStateOwner<State> by DefaultConfigurableStateOwner() {

    override fun produce(action: Action): Observable<Effect> {
        return when (action) {
            Action.ExecuteInitialize -> initialize()
            Action.ExecuteMultipleChoiceDevice -> Observable.just(Effect.StartChoiceMultipleDevice)
            Action.ExecuteCancelMultipleChoiceDevice -> Observable.just(Effect.CancelChoiceMultipleDevice)
            is Action.ExecuteSuccessChoiceMultipleDevice -> successChoiceMultipleDevice(state, action)
            is Action.ExecuteSendBarcode -> sendBarcode(state, action)
            is Action.ExecuteSelectDevice -> changeSelectedDevice(state, action)
            is Action.ExecuteResetSelectDevice -> resetSelectedDevice(state, action)
        }
    }

    private fun initialize(): Observable<Effect> {
        return Maybe.fromCallable<String> { settingsStorage.getBarcodeReceiverKey() }
            .toObservable()
            .flatMap { barcodeReceiverKey ->
                when (barcodeReceiverKey.isBlank()) {
                    true -> Observable.just(
                        Effect.FailureInitialization(
                            throwable = IllegalArgumentException("barcode receiver key is blank"),
                            errorReason = ErrorReason.EMPTY_BARCODE_RECEIVER_KEY
                        )
                    )
                    false -> Observable.just(Effect.SuccessInitialization(barcodeReceiverKey))
                }
            }
            .switchIfEmpty(
                Observable.just(
                    Effect.FailureInitialization(
                        throwable = IllegalArgumentException("barcode receiver key is empty"),
                        errorReason = ErrorReason.UNINITIALIZED_BARCODE_RECEIVER_KEY
                    )
                )
            )
            .onErrorReturn { Effect.FailureInitialization(it, ErrorReason.UNKNOWN) }
            .startWithItem(Effect.StartInitialization)
            .subscribeOn(schedulersContainer.io.get())
    }

    private fun successChoiceMultipleDevice(
        state: State,
        action: Action.ExecuteSuccessChoiceMultipleDevice
    ): Observable<Effect> {
        val devices = action.devices

        return when (devices.size) {
            0 -> resetSelectedDevice(state, Action.ExecuteResetSelectDevice)
            1 -> changeSelectedDevice(state, devices.first().run(Action::ExecuteSelectDevice))
            else -> Observable.just(Effect.SuccessChoiceMultipleDevice(devices))
        }
    }

    private fun resetSelectedDevice(state: State, action: Action.ExecuteResetSelectDevice): Observable<Effect> {
        if (state !is State.HaveSelection && state !is State.HaveMultipleChoiceDevices) return Observable.empty()
        return Observable.just(Effect.ResetSelectedDevice)
    }

    private fun changeSelectedDevice(state: State, action: Action.ExecuteSelectDevice): Observable<Effect> {
        if (state is State.ProcessSendBarcode && state !is State.ProcessSendBarcodeMultiple) return Observable.empty()
        return Observable.just(Effect.ChangeSelectedDevice(action.device))
    }

    private fun sendBarcode(state: State, action: Action.ExecuteSendBarcode): Observable<Effect> {
        val devices = when (state) {
            is State.HaveSelection -> state.devices
            is State.HaveMultipleChoiceDevices -> state.devices
            else -> return Observable.empty()
        }

        val barcodeReceiverKey = state.barcodeReceiverKey ?: return Observable.empty()

        return devices
            ?.asSequence()
            ?.filter(IDevice::isOnline)
            ?.map { device ->
                executeCommand(
                    device,
                    barcodeReceiverKey = barcodeReceiverKey,
                    barcode = action.value
                )
                    .toSingleDefault(Result.success(Unit))
                    .onErrorReturn { Result.failure(it) }
            }
            ?.toList()
            ?.takeUnless { result -> result.isEmpty() }
            ?.let { completables ->
                Single.zip(completables) { resultList ->
                    resultList
                        .find { it is Result<*> && it.isFailure }
                        ?.let { result ->
                            result as Result<*>
                            Effect.FailureSendBarcode(result.exceptionOrNull()!!)
                        }
                        ?: Effect.SuccessSendBarcode
                }
            }
            ?.run { Observable.fromSingle(this) }
            ?.startWithItem(Effect.StartSendBarcode(barcode = action.value))
            ?: Observable.empty()
    }

    private fun executeCommand(device: IDevice, barcodeReceiverKey: String, barcode: String): Completable {
        return Completable.fromCallable {
            device.executeShellCommand(
                "am broadcast -a $barcodeReceiverKey --es EXTRA_STRING_BARCODE \"${barcode}\"",
                NullOutputReceiver.getReceiver()
            )
        }.subscribeOn(schedulersContainer.io.get())
    }
}