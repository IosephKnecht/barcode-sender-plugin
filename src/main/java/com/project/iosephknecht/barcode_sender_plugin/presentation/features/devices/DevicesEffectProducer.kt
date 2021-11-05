package com.project.iosephknecht.barcode_sender_plugin.presentation.features.devices

import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.ConfigurableStateOwner
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.DefaultConfigurableStateOwner
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.devices.DevicesFeatureContract.Effect
import io.reactivex.rxjava3.core.Observable

/**
 * Implementation effect producer for devices list.
 *
 * @author IosephKnecht
 */
internal class DevicesEffectProducer : DevicesFeatureContract.Producer,
    ConfigurableStateOwner<DevicesFeatureContract.State> by DefaultConfigurableStateOwner() {

    override fun produce(action: DevicesFeatureContract.Action): Observable<Effect> {
        return when (action) {
            is DevicesFeatureContract.Action.SetDebugBridge -> Observable.just(action.bridge)
                .filter { it.isConnected }
                .map<Effect> { bridge ->
                    bridge.devices.asSequence()
                        .filter { it.isOnline }
                        .map(DevicesFeatureContract::Device)
                        .toList()
                        .let { devices -> Effect.SuccessLoadDevices(bridge, devices) }
                }
                .onErrorReturn { Effect.FailureLoadDevices(it) }
                .startWithItem(Effect.StartLoadDevices)
            DevicesFeatureContract.Action.InvokeReload -> state.debugBridge
                ?.takeIf { it.isConnected }
                ?.let { bridge ->
                    Observable.just(bridge)
                        .filter { it.isConnected }
                        .map<Effect> { bridge ->
                            bridge.devices.asSequence()
                                .filter { it.isOnline }
                                .map(DevicesFeatureContract::Device)
                                .toList()
                                .let { devices -> Effect.SuccessLoadDevices(bridge, devices) }
                        }
                        .onErrorReturn { Effect.FailureLoadDevices(it) }
                        .startWithItem(Effect.StartLoadDevices)
                }
                ?: Observable.empty()
        }
    }
}