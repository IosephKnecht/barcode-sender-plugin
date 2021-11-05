package com.project.iosephknecht.barcode_sender_plugin.presentation.features.recent_barcode_type

import com.project.iosephknecht.barcode_sender_plugin.data.BarcodeType
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.recent_barcode_type.RecentBarcodeTypeFeatureContract.Reducer
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.recent_barcode_type.RecentBarcodeTypeFeatureContract.State
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.recent_barcode_type.RecentBarcodeTypeFeatureContract.Effect

/**
 * Default implementation [Reducer].
 *
 * @author IosephKnecht
 */
internal class RecentBarcodeTypeReducerDefault : Reducer {

    override fun reduce(
        state: State,
        effect: Effect
    ): State {
        return when (state) {
            is State.NotInitialized -> state + effect
            is State.ProcessInitialized -> state + effect
            is State.Data -> state + effect
            is State.ProcessChangeRecent -> state + effect
        }
    }

    private operator fun State.NotInitialized.plus(effect: Effect): State {
        return when (effect) {
            Effect.StartInitialize -> State.ProcessInitialized
            is Effect.StartChangeRecent,
            is Effect.FailureChangeRecent,
            is Effect.FailureInitialize,
            is Effect.SuccessChangeRecent,
            is Effect.SuccessInitialize -> this
        }
    }

    private operator fun State.ProcessInitialized.plus(effect: Effect): State {
        return when (effect) {
            is Effect.SuccessInitialize -> State.Data(effect.recent)
            is Effect.FailureInitialize -> State.Data(BarcodeType.values().associateWith { 0 })
            Effect.StartInitialize,
            is Effect.StartChangeRecent,
            is Effect.FailureChangeRecent,
            is Effect.SuccessChangeRecent -> this
        }
    }

    private operator fun State.Data.plus(effect: Effect): State {
        return when (effect) {
            is Effect.StartChangeRecent -> State.ProcessChangeRecent(
                recent = this.recent,
                barcodeType = effect.barcodeType
            )
            Effect.StartInitialize,
            is Effect.FailureChangeRecent,
            is Effect.FailureInitialize,
            is Effect.SuccessChangeRecent,
            is Effect.SuccessInitialize -> this
        }
    }

    private operator fun State.ProcessChangeRecent.plus(effect: Effect): State {
        return when (effect) {
            is Effect.FailureChangeRecent -> State.Data(recent)
            is Effect.SuccessChangeRecent -> State.Data(effect.recent)
            Effect.StartInitialize,
            is Effect.FailureInitialize,
            is Effect.StartChangeRecent,
            is Effect.SuccessInitialize -> this
        }
    }
}