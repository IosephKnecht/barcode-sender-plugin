package com.project.iosephknecht.barcode_sender_plugin.presentation.multiple_choice.component

import com.intellij.openapi.diagnostic.Logger
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.SchedulersContainer
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.Swing
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.states
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.devices.devices
import hu.akarnokd.rxjava3.swing.SwingSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.devices.DevicesFeatureContract.Feature as DevicesFeature
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.devices.DevicesFeatureContract.Intent as DevicesIntent
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.devices_selector.DevicesSelectorFeatureContract.Feature as DevicesSelector
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.devices_selector.DevicesSelectorFeatureContract.Intent as DevicesSelectorIntent

/**
 * Default implementation for [MultipleChoiceComponent].
 *
 * @param devicesFeature
 * @param devicesSelector
 *
 * @author IosephKnecht
 */
internal class MultipleChoiceComponentDefault(
    private val devicesFeature: DevicesFeature,
    private val devicesSelector: DevicesSelector,
    private val logger: Logger,
    schedulersContainer: SchedulersContainer = SchedulersContainer.Swing
) : MultipleChoiceComponent {

    private val featuresDisposable = CompositeDisposable()
    private val viewsDisposable = CompositeDisposable()

    override val states: BehaviorSubject<MultipleChoiceComponent.State> = BehaviorSubject.create()
    override val events: PublishSubject<MultipleChoiceComponent.Event> = PublishSubject.create()

    init {
        Observable.combineLatest(
            devicesFeature.states.distinctUntilChanged(),
            devicesSelector.states.distinctUntilChanged()
        ) { devicesState, selectorState ->
            val devices = devicesState.devices.orEmpty()
            val selectedDevices = selectorState.devices

            val list = devices.map { device ->
                MultipleChoiceComponent.Device(
                    device = device,
                    isSelected = selectedDevices.contains(device)
                )
            }

            MultipleChoiceComponent.State(
                deviceList = list,
                selectedDeviceCount = selectorState.devices.size
            )
        }
            .doOnSubscribe {
                devicesFeature.let(featuresDisposable::add)
                devicesSelector.let(featuresDisposable::add)
                devicesFeature.accept(DevicesIntent.Reload)
            }
            .subscribeOn(schedulersContainer.computation.get())
            .observeOn(SwingSchedulers.edt())
            .subscribe(
                states::onNext,
                logger::error
            )
            .let(featuresDisposable::add)
    }

    override fun bindView(view: MultipleChoiceComponent.View) {
        view.selectDeviceObservable
            .map { DevicesSelectorIntent.SetSelection(it.device) }
            .subscribe(
                devicesSelector::accept,
                logger::error
            )
            .let(viewsDisposable::add)

        view.resetSelectDeviceObservable
            .map { DevicesSelectorIntent.ResetSelection(it.device) }
            .subscribe(
                devicesSelector::accept,
                logger::error
            )
            .let(viewsDisposable::add)

        view.successChoiceObservable
            .debounce(300, TimeUnit.MILLISECONDS)
            .map { MultipleChoiceComponent.Event.SuccessChoice(devicesSelector.state.devices) }
            .observeOn(SwingSchedulers.edt())
            .subscribe(
                events::onNext,
                logger::error
            )
            .let(viewsDisposable::add)

        view.interruptChoiceObservable
            .debounce(300, TimeUnit.MILLISECONDS)
            .map { MultipleChoiceComponent.Event.InterruptChoice }
            .observeOn(SwingSchedulers.edt())
            .subscribe(
                events::onNext,
                logger::error
            )
            .let(viewsDisposable::add)
    }

    override fun unbindView() {
        viewsDisposable.clear()
    }

    override fun dispose() {
        featuresDisposable.clear()
        states.onComplete()
    }
}