package com.project.iosephknecht.barcode_sender_plugin.presentation.features.recent_barcode_type

import com.project.iosephknecht.barcode_sender_plugin.data.BarcodeType
import com.project.iosephknecht.barcode_sender_plugin.domain.LocalStorage
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.ConfigurableStateOwner
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.DefaultConfigurableStateOwner
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.recent_barcode_type.RecentBarcodeTypeFeatureContract.Producer
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.recent_barcode_type.RecentBarcodeTypeFeatureContract.State
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.recent_barcode_type.RecentBarcodeTypeFeatureContract.Action
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.recent_barcode_type.RecentBarcodeTypeFeatureContract.Effect
import hu.akarnokd.rxjava3.swing.SwingSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * Default implementation [Producer].
 *
 * @author IosephKnecht
 */
internal class RecentBarcodeTypeProducerDefault(
    private val localStorage: LocalStorage
) : Producer,
    ConfigurableStateOwner<State> by DefaultConfigurableStateOwner() {

    override fun produce(action: Action): Observable<Effect> {
        return when (action) {
            Action.ExecuteInitialize -> initialize()
            is Action.ExecuteChoiceBarcodeType -> choiceBarcode(state, action.barcodeType)
        }
    }

    private fun initialize(): Observable<Effect> {
        return Observable.just(localStorage.getRecentBarcodeType().orEmpty())
            .map<Effect> { map ->
                val mutableMap = LinkedHashMap(map)
                BarcodeType.values().forEach { barcodeType -> mutableMap.putIfAbsent(barcodeType, 0L) }

                mutableMap.toList()
                    .sortedByDescending { it.second }
                    .toMap()
                    .run(Effect::SuccessInitialize)
            }
            .onErrorReturn { Effect.FailureInitialize(it) }
            .startWithItem(Effect.StartInitialize)
            .subscribeOn(Schedulers.computation())
    }

    private fun choiceBarcode(state: State, barcodeType: BarcodeType): Observable<Effect> {
        val currentRecent = when (state) {
            is State.Data -> state.recent
            is State.ProcessChangeRecent -> state.recent
            State.NotInitialized,
            State.ProcessInitialized -> return Observable.empty()
        }

        return Observable.fromCallable<LinkedHashMap<BarcodeType, Long>> { currentRecent.run(::LinkedHashMap) }
            .map { recentMap ->
                recentMap[barcodeType] = System.currentTimeMillis()
                recentMap
            }
            .flatMap { recentMap ->
                val shallowRecentMap = recentMap
                    .toList()
                    .sortedByDescending { it.second }
                    .toMap()

                Observable.fromCallable { localStorage.setRecentBarcodeType(LinkedHashMap(shallowRecentMap)) }
                    .map<Effect> { Effect.SuccessChangeRecent(shallowRecentMap) }
                    .onErrorReturn { Effect.FailureChangeRecent(it) }
                    .startWithItem(Effect.StartChangeRecent(barcodeType))
                    .subscribeOn(SwingSchedulers.edt())
            }
            .subscribeOn(Schedulers.computation())
    }
}