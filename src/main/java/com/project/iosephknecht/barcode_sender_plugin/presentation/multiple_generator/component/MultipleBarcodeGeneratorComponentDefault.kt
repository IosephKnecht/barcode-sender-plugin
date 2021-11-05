package com.project.iosephknecht.barcode_sender_plugin.presentation.multiple_generator.component

import com.intellij.openapi.diagnostic.Logger
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_generator_multiple.*
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.news
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.states
import com.project.iosephknecht.barcode_sender_plugin.presentation.multiple_generator.component.MultipleBarcodeGeneratorComponent.Event
import com.project.iosephknecht.barcode_sender_plugin.presentation.multiple_generator.component.MultipleBarcodeGeneratorComponent.State
import hu.akarnokd.rxjava3.swing.SwingSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit

/**
 * Implementation [MultipleBarcodeGeneratorComponent].
 *
 * @param multipleBarcodeGeneratorFeature
 *
 * @author IosephKnecht.
 */
internal class MultipleBarcodeGeneratorComponentDefault(
    private val multipleBarcodeGeneratorFeature: BarcodeGeneratorMultipleFeatureContract.Feature,
    private val logger: Logger
) : MultipleBarcodeGeneratorComponent {

    private val featureDisposables = CompositeDisposable()
    private val viewDisposables = CompositeDisposable()

    override val state: BehaviorSubject<State> = BehaviorSubject.create()
    override val events: PublishSubject<Event> = PublishSubject.create()

    init {
        featureDisposables.add(multipleBarcodeGeneratorFeature)

        multipleBarcodeGeneratorFeature.news
            .ofType(BarcodeGeneratorMultipleFeatureContract.News.ApproveBarcodeList::class.java)
            .map { news ->
                when (val barcodeList = news.barcodeList) {
                    null -> Event.CancelBarcodeList
                    else -> Event.ApproveBarcodeList(barcodeList)
                }
            }
            .subscribe(
                events::onNext,
                logger::error
            )
            .let(featureDisposables::add)

        multipleBarcodeGeneratorFeature.states
            .distinctUntilChanged()
            .map { state ->
                State(
                    barcodeCount = state.barcodeCount,
                    minCount = state.minBarcodeCount,
                    maxCount = state.maxBarcodeCount,
                    barcodeType = state.barcodeType,
                    barcodeText = state.barcodeList?.list?.joinToString(separator = System.lineSeparator()).orEmpty(),
                    isHaveBarcodeList = state.isHaveBarcodeList,
                    isProcessGenerate = state.isProcessGenerating
                )
            }
            .subscribeOn(Schedulers.computation())
            .observeOn(SwingSchedulers.edt())
            .subscribe(
                state::onNext,
                logger::error
            )
            .let(featureDisposables::add)
    }

    override fun bindView(view: MultipleBarcodeGeneratorComponent.View) {
        view.countObservable
            .debounce(300, TimeUnit.MILLISECONDS)
            .map(BarcodeGeneratorMultipleFeatureContract.Intent::SetBarcodeCount)
            .observeOn(SwingSchedulers.edt())
            .subscribe(
                multipleBarcodeGeneratorFeature::accept,
                logger::error
            )
            .let(viewDisposables::add)

        view.barcodeTypeObservable
            .debounce(300, TimeUnit.MILLISECONDS)
            .map(BarcodeGeneratorMultipleFeatureContract.Intent::SetBarcodeType)
            .observeOn(SwingSchedulers.edt())
            .subscribe(
                multipleBarcodeGeneratorFeature::accept,
                logger::error
            )
            .let(viewDisposables::add)

        view.generateBarcodeList
            .debounce(300, TimeUnit.MILLISECONDS)
            .map { BarcodeGeneratorMultipleFeatureContract.Intent.GenerateBarcodesList }
            .observeOn(SwingSchedulers.edt())
            .subscribe(
                multipleBarcodeGeneratorFeature::accept,
                logger::error
            )
            .let(viewDisposables::add)

        view.approveList
            .debounce(300, TimeUnit.MILLISECONDS)
            .map { BarcodeGeneratorMultipleFeatureContract.Intent.ApproveBarcodeList }
            .observeOn(SwingSchedulers.edt())
            .subscribe(
                multipleBarcodeGeneratorFeature::accept,
                logger::error
            )
            .let(viewDisposables::add)

        view.closeObservable
            .debounce(300, TimeUnit.MILLISECONDS)
            .map { Event.CancelBarcodeList }
            .observeOn(SwingSchedulers.edt())
            .subscribe(
                events::onNext,
                logger::error
            )
            .let(viewDisposables::add)
    }

    override fun unbindView() {
        viewDisposables.clear()
    }

    override fun dispose() {
        featureDisposables.clear()
    }
}