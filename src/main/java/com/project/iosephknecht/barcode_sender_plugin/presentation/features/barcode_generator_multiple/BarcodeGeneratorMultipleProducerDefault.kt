package com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_generator_multiple

import com.project.iosephknecht.barcode_sender_plugin.data.BarcodeType
import com.project.iosephknecht.barcode_sender_plugin.domain.BarcodeGenerator
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.ConfigurableStateOwner
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.DefaultConfigurableStateOwner
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_generator_multiple.BarcodeGeneratorMultipleFeatureContract.Producer
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_generator_multiple.BarcodeGeneratorMultipleFeatureContract.State
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_generator_multiple.BarcodeGeneratorMultipleFeatureContract.Action
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_generator_multiple.BarcodeGeneratorMultipleFeatureContract.Effect
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_generator_multiple.BarcodeGeneratorMultipleFeatureContract.BarcodeList
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.SchedulersContainer
import io.reactivex.rxjava3.core.Observable

/**
 * Default implementation [Producer].
 *
 * @param barcodeGenerator instance of generator.
 *
 * @author IosephKnecht
 */
internal class BarcodeGeneratorMultipleProducerDefault(
    private val barcodeGenerator: BarcodeGenerator,
    private val schedulersContainer: SchedulersContainer
) : Producer,
    ConfigurableStateOwner<State> by DefaultConfigurableStateOwner() {

    override fun produce(action: Action): Observable<Effect> {
        return when (action) {
            Action.ExecuteGenerateBarcodeList -> generateBarcodeList(state)
            Action.ExecuteApproveList -> Observable.just(Effect.ApproveBarcodeList)
            is Action.ExecuteSetBarcodeCount -> setBarcodeCount(state, action.barcodeCount)
            is Action.ExecuteSetBarcodeType -> setBarcodeType(state, action.barcodeType)
        }
    }

    private fun generateBarcodeList(state: State): Observable<Effect> {
        val barcodeType = state.barcodeType
        val barcodeCount = state.barcodeCount

        return Observable.fromIterable(0 until barcodeCount)
            .map { barcodeGenerator.generate(barcodeType) }
            .toList()
            .toObservable()
            .map<Effect> { BarcodeList(barcodeType, ArrayList(it)).run(Effect::SuccessGenerateBarcodeList) }
            .onErrorReturn { Effect.FailureGenerateBarcodeList(it) }
            .startWithItem(Effect.StartGenerateBarcodeList)
            .subscribeOn(schedulersContainer.io.get())
    }

    private fun setBarcodeCount(state: State, barcodeCount: Int): Observable<Effect> {
        val previousBarcodeCount = state.barcodeCount
        if (previousBarcodeCount == barcodeCount) return Observable.empty()

        val min = state.minBarcodeCount
        val max = state.maxBarcodeCount

        val isInvalid = when {
            barcodeCount <= 0 -> true
            barcodeCount < min -> true
            barcodeCount > max -> true
            else -> false
        }

        if (isInvalid) return Observable.just(Effect.FailureConstraintBarcodeCount)

        return Observable.just(Effect.ChangeBarcodeCount(barcodeCount))
    }

    private fun setBarcodeType(state: State, barcodeType: BarcodeType): Observable<Effect> {
        val previousBarcodeType = state.barcodeType
        if (previousBarcodeType == barcodeType) return Observable.empty()

        return Observable.just(Effect.ChangeBarcodeType(barcodeType))
    }
}