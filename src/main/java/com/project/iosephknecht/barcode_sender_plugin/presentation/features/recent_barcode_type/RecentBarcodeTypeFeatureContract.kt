package com.project.iosephknecht.barcode_sender_plugin.presentation.features.recent_barcode_type

import com.project.iosephknecht.barcode_sender_plugin.data.BarcodeType
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.EffectProducer
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.StateReducer
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.Store

/**
 * Contract for recent barcode type feature.
 *
 * @author IosephKnecht
 */
internal interface RecentBarcodeTypeFeatureContract {

    /**
     * Feature contract for recent barcode type.
     *
     * @author IosephKnecht
     */
    interface Feature : Store<State, Intent, News>

    /**
     * Reducer contract for recent barcode type.
     *
     * @author IosephKnecht
     */
    interface Reducer : StateReducer<State, Effect>

    /**
     * Producer contract for recent barcode type.
     *
     * @author IosephKnecht
     */
    interface Producer : EffectProducer<State, Action, Effect>

    /**
     * Bootstrapper contract for recent barcode type.
     *
     * @author IosephKnecht
     */
    interface Bootstrapper : com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.Bootstrapper<State, Action>

    /**
     * ActionMapper contract for recent barcode type.
     *
     * @author IosephKnecht
     */
    interface ActionMapper : com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.ActionMapper<State, Intent, Action>

    /**
     * NewsMapper contract for recent barcode type.
     *
     * @author IosephKnecht
     */
    interface NewsMapper : com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.NewsMapper<State, Effect, News>

    /**
     * Contract for state of recent barcode type feature.
     *
     * @author IosephKnecht
     */
    sealed interface State {
        /**
         * Not initialized - state not restored.
         */
        object NotInitialized : State

        /**
         * Process restore state.
         */
        object ProcessInitialized : State

        /**
         * Success restored state. Waiting change recent.
         *
         * @param recent map of recent values.
         */
        data class Data(val recent: Map<BarcodeType, Long>) : State

        /**
         * Process change recent state.
         *
         * @param recent map of recent values.
         * @param barcodeType type of barcode for change.
         */
        data class ProcessChangeRecent(
            val recent: Map<BarcodeType, Long>,
            val barcodeType: BarcodeType
        ) : State
    }

    /**
     * Contract for user wish.
     *
     * @author IosephKnecht
     */
    sealed interface Intent {
        /**
         * Initialize - restore state.
         */
        object Initialize : Intent

        /**
         * Change recent map.
         *
         * @param barcodeType type of barcode.
         */
        class ChoiceBarcodeType(val barcodeType: BarcodeType) : Intent
    }

    /**
     * Contract for feature action.
     *
     * @author IosephKnecht
     */
    sealed interface Action {
        /**
         * Execute initialize.
         */
        object ExecuteInitialize : Action

        /**
         * Execute change recent map.
         */
        class ExecuteChoiceBarcodeType(val barcodeType: BarcodeType) : Action
    }

    /**
     * Contract for effect.
     *
     * @author IosephKnecht.
     */
    sealed interface Effect {
        /**
         * Effect for mark start restored state.
         */
        object StartInitialize : Effect

        /**
         * Effect for mark failure restore state.
         */
        class FailureInitialize(val throwable: Throwable) : Effect

        /**
         * Effect for mark success restore state.
         *
         * @param recent
         */
        class SuccessInitialize(val recent: Map<BarcodeType, Long>) : Effect

        /**
         * Effect for mark start change recent map.
         *
         * @param barcodeType type of barcode.
         */
        class StartChangeRecent(val barcodeType: BarcodeType) : Effect

        /**
         * Effect for mark failure change recent map.
         *
         * @param throwable
         */
        class FailureChangeRecent(val throwable: Throwable) : Effect

        /**
         * Effect for mark success change recent map.
         *
         * @param recent
         */
        class SuccessChangeRecent(val recent: Map<BarcodeType, Long>) : Effect
    }

    /**
     * Contract for feature news.
     *
     * @author IosephKnecht
     */
    sealed interface News {
        /**
         * News of failure initialize.
         *
         * @param throwable
         */
        class FailureInitialize(val throwable: Throwable) : News

        /**
         * News of failure change initialize.
         *
         * @param throwable
         */
        class FailureRecentChange(val throwable: Throwable) : News
    }
}