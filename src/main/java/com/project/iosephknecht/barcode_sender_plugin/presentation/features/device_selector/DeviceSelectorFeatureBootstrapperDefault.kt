package com.project.iosephknecht.barcode_sender_plugin.presentation.features.device_selector

import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.ConfigurableStateOwner
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.DefaultConfigurableStateOwner
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.SchedulersContainer
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.news
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.device_selector.DeviceSelectorFeatureContract.*
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.devices.DevicesFeatureContract
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.devices.devices
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer

/**
 * Implementation for [DeviceSelectorFeatureContract.Bootstrapper]
 *
 * @param devicesFeature
 *
 * @author IosephKnecht
 */
internal class DeviceSelectorFeatureBootstrapperDefault(
    private val devicesFeature: DevicesFeatureContract.Feature,
    private val schedulersContainer: SchedulersContainer
) : Bootstrapper,
    ConfigurableStateOwner<State> by DefaultConfigurableStateOwner() {

    override fun subscribe(observer: Observer<in Action>) {
        devicesFeature.news
            .ofType(DevicesFeatureContract.News.DeviceListChanged::class.java)
            .switchMap {
                val devicesState = devicesFeature.state
                val deviceSelectorState = state

                val minSelectedCount = when (deviceSelectorState) {
                    State.NotInitialized,
                    State.ProcessInitialize,
                    is State.FailureInitialize,
                    is State.NotHaveSelection,
                    is State.WaitingChoiceMultipleDevice -> return@switchMap Observable.empty()
                    is State.HaveSelection,
                    is State.ProcessSendBarcode -> 1
                    is State.HaveMultipleChoiceDevices,
                    is State.ProcessSendBarcodeMultiple -> 2
                }

                val allDevices = devicesState.devices.orEmpty()
                val selectedDevices = deviceSelectorState.devices ?: return@switchMap Observable.empty()

                val confirmedSelectedDevice = selectedDevices
                    .filter { selectedDevice -> allDevices.contains(selectedDevice) }

                when (confirmedSelectedDevice.size < minSelectedCount) {
                    true -> Observable.just(Action.ExecuteResetSelectDevice)
                    false -> Observable.empty()
                }
            }
            .subscribeOn(schedulersContainer.computation.get())
            .subscribe(observer)
    }
}