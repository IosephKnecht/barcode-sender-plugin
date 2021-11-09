package com.project.iosephknecht.barcode_sender_plugin.presentation.features.devices_selector

import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.*
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.ConfigurableStateOwner
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.DefaultConfigurableStateOwner
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.SchedulersContainer
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.news
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.states
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.device_selector.DeviceSelectorFeatureContract
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.devices.DevicesFeatureContract
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.devices.devices
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.devices_selector.DevicesSelectorFeatureContract.*
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.devices_selector.DevicesSelectorFeatureContract.Bootstrapper
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer

/**
 * Implementation for [Bootstrapper].
 *
 * @param devicesFeature
 * @param deviceSelectorFeature
 *
 * @author IosephKnecht
 */
internal class DevicesSelectorBootstrapperDefault(
    private val devicesFeature: DevicesFeatureContract.Feature,
    private val deviceSelectorFeature: DeviceSelectorFeatureContract.Feature,
    private val schedulersContainer: SchedulersContainer
) : Bootstrapper,
    ConfigurableStateOwner<State> by DefaultConfigurableStateOwner() {

    override fun subscribe(observer: Observer<in Intent>) {
        val devicesActions = devicesFeature.states
            .map { state -> state.devices?.toSet().orEmpty() }
            .distinctUntilChanged()
            .map { devices ->
                val selectedDevices = state.devices
                selectedDevices.filterNot(devices::contains)
            }
            .filter { it.isNotEmpty() }
            .map { Intent.ResetSelection(*it.toTypedArray()) }
            .subscribeOn(schedulersContainer.computation.get())

        val selectorActions = deviceSelectorFeature.news
            .filter { news ->
                news is DeviceSelectorFeatureContract.News.SingleChoiceDevice ||
                    news is DeviceSelectorFeatureContract.News.ResetSelected
            }
            .map { Intent.ResetSelection(*state.devices.toTypedArray()) }
            .subscribeOn(schedulersContainer.computation.get())

        Observable.merge(devicesActions, selectorActions)
            .subscribe(observer)
    }
}