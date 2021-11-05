package com.project.iosephknecht.barcode_sender_plugin.presentation.features.recent_barcode_type

import com.project.iosephknecht.barcode_sender_plugin.presentation.features.recent_barcode_type.RecentBarcodeTypeFeatureContract.NewsMapper
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.recent_barcode_type.RecentBarcodeTypeFeatureContract.State
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.recent_barcode_type.RecentBarcodeTypeFeatureContract.Effect
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.recent_barcode_type.RecentBarcodeTypeFeatureContract.News

/**
 * Default implementation [NewsMapper].
 *
 * @author IosephKnecht
 */
internal class RecentBarcodeTypeNewsMapperDefault : NewsMapper {

    override fun map(
        state: State,
        effect: Effect
    ): News? {
        return when (state) {
            State.NotInitialized -> when (effect) {
                Effect.StartInitialize,
                is Effect.FailureInitialize,
                is Effect.FailureChangeRecent,
                is Effect.StartChangeRecent,
                is Effect.SuccessChangeRecent,
                is Effect.SuccessInitialize -> null
            }
            State.ProcessInitialized -> when (effect) {
                is Effect.FailureInitialize -> News.FailureInitialize(effect.throwable)
                Effect.StartInitialize,
                is Effect.FailureChangeRecent,
                is Effect.StartChangeRecent,
                is Effect.SuccessChangeRecent,
                is Effect.SuccessInitialize -> null
            }
            is State.Data -> when (effect) {
                Effect.StartInitialize,
                is Effect.FailureInitialize,
                is Effect.FailureChangeRecent,
                is Effect.StartChangeRecent,
                is Effect.SuccessChangeRecent,
                is Effect.SuccessInitialize -> null
            }
            is State.ProcessChangeRecent -> when (effect) {
                is Effect.FailureChangeRecent -> News.FailureRecentChange(effect.throwable)
                Effect.StartInitialize,
                is Effect.FailureInitialize,
                is Effect.StartChangeRecent,
                is Effect.SuccessChangeRecent,
                is Effect.SuccessInitialize -> null
            }
        }
    }
}