package com.project.iosephknecht.barcode_sender_plugin.presentation.features.devices

import com.android.ddmlib.AndroidDebugBridge
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.devices.DevicesFeatureContract.Action
import com.project.iosephknecht.barcode_sender_plugin.presentation.utils.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer

/**
 * Implementation bootstrapper for devices list.
 *
 * @author IosephKnecht
 */
internal class DevicesBootstrapper(
    private val debugBridgeChangeListener: Observable<AndroidDebugBridge> = debugBridgeChangeListener(),
    private val devicesChangeListener: Observable<IDeviceChangeInfo> = deviceChangeListener(),
    private val clientChangeListener: Observable<IClientChangeInfo> = clientChangeListener()
) : DevicesFeatureContract.Bootstrapper {

    override fun subscribe(observer: Observer<in Action>) {
        Observable.merge(
            debugBridgeChangeListener.map(Action::SetDebugBridge),
            devicesChangeListener.map { Action.InvokeReload },
            clientChangeListener.map { Action.InvokeReload }
        ).subscribe(observer)
    }
}