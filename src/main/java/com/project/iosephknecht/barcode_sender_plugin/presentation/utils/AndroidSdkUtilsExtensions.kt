package com.project.iosephknecht.barcode_sender_plugin.presentation.utils

import com.android.ddmlib.AndroidDebugBridge
import com.android.ddmlib.Client
import com.android.ddmlib.IDevice
import io.reactivex.rxjava3.core.Observable

/**
 * Device name for view.
 *
 * Can't believe the avdName won't be null.
 * The IDevice implementation ensures that the serialNumber always not-null,
 * but a call orEmpty is necessary because IDevice is an interface.
 */
internal val IDevice.deviceName: String
    get() = (name ?: avdName ?: serialNumber).orEmpty()

internal fun debugBridgeChangeListener(): Observable<AndroidDebugBridge> {
    return Observable.create { emitter ->
        runCatching { AndroidDebugBridge.getBridge() }
            .onFailure(emitter::onError)
            .onSuccess { bridge ->
                if (!emitter.isDisposed && bridge != null) emitter.onNext(bridge)
            }

        val listener = AndroidDebugBridge.IDebugBridgeChangeListener { bridge ->
            if (!emitter.isDisposed && bridge != null) emitter.onNext(bridge)
        }

        emitter.setCancellable {
            runCatching { AndroidDebugBridge.removeDebugBridgeChangeListener(listener) }
                .onFailure(emitter::onError)
        }

        runCatching { AndroidDebugBridge.addDebugBridgeChangeListener(listener) }
            .onFailure(emitter::onError)
    }
}

internal fun deviceChangeListener(): Observable<IDeviceChangeInfo> {
    return Observable.create { emitter ->
        val listener = object : AndroidDebugBridge.IDeviceChangeListener {
            override fun deviceConnected(device: IDevice?) {
                if (!emitter.isDisposed && device != null)
                    device.run(IDeviceChangeInfo::Connected).let(emitter::onNext)
            }

            override fun deviceDisconnected(device: IDevice?) {
                if (!emitter.isDisposed && device != null)
                    device.run(IDeviceChangeInfo::Disconnected).let(emitter::onNext)
            }

            override fun deviceChanged(device: IDevice?, changeMask: Int) {
                if (!emitter.isDisposed && device != null)
                    IDeviceChangeInfo.DeviceChanged(device, changeMask).let(emitter::onNext)
            }

        }

        emitter.setCancellable {
            runCatching { AndroidDebugBridge.removeDeviceChangeListener(listener) }
                .onFailure(emitter::onError)
        }

        runCatching { AndroidDebugBridge.addDeviceChangeListener(listener) }
            .onFailure(emitter::onError)
    }
}

internal fun clientChangeListener(): Observable<IClientChangeInfo> {
    return Observable.create { emitter ->
        val listener = AndroidDebugBridge.IClientChangeListener { client: Client?, changeMask: Int ->
            if (!emitter.isDisposed && client != null)
                IClientChangeInfo(client, changeMask).let(emitter::onNext)
        }

        emitter.setCancellable {
            runCatching { AndroidDebugBridge.addClientChangeListener(listener) }
                .onFailure(emitter::onError)
        }

        runCatching { AndroidDebugBridge.addClientChangeListener(listener) }
            .onFailure(emitter::onError)
    }
}

internal sealed interface IDeviceChangeInfo {
    class Connected(val device: IDevice) : IDeviceChangeInfo
    class Disconnected(val device: IDevice) : IDeviceChangeInfo
    @Suppress("unused")
    class DeviceChanged(val device: IDevice, val changeMask: Int) : IDeviceChangeInfo
}

@Suppress("unused")
internal class IClientChangeInfo(val client: Client, changeMask: Int)