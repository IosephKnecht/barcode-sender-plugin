package com.project.iosephknecht.barcode_sender_plugin.presentation.settings.component

import com.intellij.openapi.diagnostic.Logger
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.SchedulersContainer
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.Swing
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.states
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.settings.*
import hu.akarnokd.rxjava3.swing.SwingSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit

/**
 * Default implementation [SettingsComponent].
 *
 * @param settingsFeature
 *
 * @author IosephKnecht
 */
internal class SettingsComponentDefault(
    private val settingsFeature: SettingsFeatureContract.Feature,
    private val logger: Logger,
    schedulersContainer: SchedulersContainer = SchedulersContainer.Swing
) : SettingsComponent {

    private val featureDisposables = CompositeDisposable()
    private val viewDisposables = CompositeDisposable()

    override val states: BehaviorSubject<SettingsComponent.State> = BehaviorSubject.create()
    override val events: PublishSubject<SettingsComponent.Event> = PublishSubject.create()
    override val state: SettingsComponent.State?
        get() = states.value

    init {
        featureDisposables.add(settingsFeature)

        settingsFeature.states
            .distinctUntilChanged()
            .map { state ->
                SettingsComponent.State(
                    barcodeReceiveKey = state.currentBarcodeReceiverKey,
                    isFailureInitialize = state.isFailureInitialize,
                    isProcessInitialize = state.isProcessInitialize,
                    isProcessSaveChanges = state.isProcessSaveChanges,
                    isChanged = state.isHaveChanges
                )
            }
            .subscribeOn(schedulersContainer.computation.get())
            .observeOn(SwingSchedulers.edt())
            .doOnSubscribe { settingsFeature.accept(SettingsFeatureContract.Intent.LoadSettings) }
            .subscribe(
                states::onNext,
                logger::error
            )
            .let(featureDisposables::add)
    }

    override fun bindView(view: SettingsComponent.View) {
        view.applySettings
            .debounce(300, TimeUnit.MILLISECONDS)
            .map { SettingsFeatureContract.Intent.SaveSettings }
            .observeOn(SwingSchedulers.edt())
            .subscribe(
                settingsFeature::accept,
                logger::error
            )
            .let(viewDisposables::add)

        view.resetSettings
            .debounce(300, TimeUnit.MILLISECONDS)
            .map { SettingsFeatureContract.Intent.ResetSettings }
            .observeOn(SwingSchedulers.edt())
            .subscribe(
                settingsFeature::accept,
                logger::error
            )
            .let(viewDisposables::add)

        view.barcodeReceiverKey
            .debounce(300, TimeUnit.MILLISECONDS)
            .map(SettingsFeatureContract.Intent::SetBarcodeReceiveKey)
            .observeOn(SwingSchedulers.edt())
            .subscribe(
                settingsFeature::accept,
                logger::error
            )
            .let(viewDisposables::add)
    }

    override fun unbindView() {
        viewDisposables.clear()
    }

    override fun release() {
        featureDisposables.clear()
    }
}