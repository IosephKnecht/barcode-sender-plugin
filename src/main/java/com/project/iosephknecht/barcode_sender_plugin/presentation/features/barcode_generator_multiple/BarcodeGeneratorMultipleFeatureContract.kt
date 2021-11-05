package com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_generator_multiple

import com.project.iosephknecht.barcode_sender_plugin.data.BarcodeType
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.EffectProducer
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.StateReducer
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.Store

/**
 * Extension - value for get minimum barcode count.
 */
internal val BarcodeGeneratorMultipleFeatureContract.State.minBarcodeCount: Int
    get() = when (this) {
        is BarcodeGeneratorMultipleFeatureContract.State.Empty -> this.minBarcodeCount
        is BarcodeGeneratorMultipleFeatureContract.State.ProcessGenerating -> this.minBarcodeCount
        is BarcodeGeneratorMultipleFeatureContract.State.Data -> this.minBarcodeCount
    }

/**
 * Extension - value for get maximum barcode count.
 */
internal val BarcodeGeneratorMultipleFeatureContract.State.maxBarcodeCount: Int
    get() = when (this) {
        is BarcodeGeneratorMultipleFeatureContract.State.Empty -> this.maxBarcodeCount
        is BarcodeGeneratorMultipleFeatureContract.State.ProcessGenerating -> this.maxBarcodeCount
        is BarcodeGeneratorMultipleFeatureContract.State.Data -> this.maxBarcodeCount
    }

/**
 * Extension - value for get current barcode count.
 */
internal val BarcodeGeneratorMultipleFeatureContract.State.barcodeCount: Int
    get() = when (this) {
        is BarcodeGeneratorMultipleFeatureContract.State.Empty -> this.currentBarcodeCount
        is BarcodeGeneratorMultipleFeatureContract.State.ProcessGenerating -> this.currentBarcodeCount
        is BarcodeGeneratorMultipleFeatureContract.State.Data -> this.currentBarcodeCount
    }

/**
 * Extension - value for get current barcode type.
 */
internal val BarcodeGeneratorMultipleFeatureContract.State.barcodeType: BarcodeType
    get() = when (this) {
        is BarcodeGeneratorMultipleFeatureContract.State.Empty -> this.barcodeType
        is BarcodeGeneratorMultipleFeatureContract.State.ProcessGenerating -> this.barcodeType
        is BarcodeGeneratorMultipleFeatureContract.State.Data -> this.barcodeType
    }

/**
 * Extension - value for get barcodes if exist.
 */
internal val BarcodeGeneratorMultipleFeatureContract.State.barcodeList: BarcodeGeneratorMultipleFeatureContract.BarcodeList?
    get() = when (this) {
        is BarcodeGeneratorMultipleFeatureContract.State.Empty,
        is BarcodeGeneratorMultipleFeatureContract.State.ProcessGenerating -> null
        is BarcodeGeneratorMultipleFeatureContract.State.Data -> this.barcodeList
    }

/**
 * Extension - value for get process generating barcode list.
 */
internal val BarcodeGeneratorMultipleFeatureContract.State.isProcessGenerating: Boolean
    get() = when (this) {
        is BarcodeGeneratorMultipleFeatureContract.State.Empty,
        is BarcodeGeneratorMultipleFeatureContract.State.Data -> false
        is BarcodeGeneratorMultipleFeatureContract.State.ProcessGenerating -> true
    }

/**
 * Extension - value for get flag if have barcodes.
 */
internal val BarcodeGeneratorMultipleFeatureContract.State.isHaveBarcodeList: Boolean
    get() = when (this) {
        is BarcodeGeneratorMultipleFeatureContract.State.Empty,
        is BarcodeGeneratorMultipleFeatureContract.State.ProcessGenerating -> false
        is BarcodeGeneratorMultipleFeatureContract.State.Data -> true
    }

/**
 * Feature contract for generate multiple barcode.
 *
 * @author IosephKnecht
 */
internal sealed interface BarcodeGeneratorMultipleFeatureContract {

    /**
     * Feature contract.
     */
    interface Feature : Store<State, Intent, News>

    /**
     * Reducer contract.
     */
    interface Reducer : StateReducer<State, Effect>

    /**
     * Producer contract.
     */
    interface Producer : EffectProducer<State, Action, Effect>

    /**
     * ActionMapper contract.
     */
    interface ActionMapper : com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.ActionMapper<State, Intent, Action>

    /**
     * NewsMapper contract.
     */
    interface NewsMapper : com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.NewsMapper<State, Effect, News>

    /**
     * Contract of states.
     *
     * @author IosephKnecht
     */
    sealed interface State {

        /**
         * Empty state, not yet generated barcode list.
         */
        data class Empty(
            val currentBarcodeCount: Int,
            val minBarcodeCount: Int,
            val maxBarcodeCount: Int,
            val barcodeType: BarcodeType
        ) : State

        /**
         * Process generate barcode list creating.
         */
        data class ProcessGenerating(
            val currentBarcodeCount: Int,
            val minBarcodeCount: Int,
            val maxBarcodeCount: Int,
            val barcodeType: BarcodeType
        ) : State

        /**
         * State after successful generate barcode list.
         */
        data class Data(
            val currentBarcodeCount: Int,
            val minBarcodeCount: Int,
            val maxBarcodeCount: Int,
            val barcodeType: BarcodeType,
            val barcodeList: BarcodeList
        ) : State
    }

    /**
     * Contract for user wishes.
     *
     * @author IosephKnecht
     */
    sealed interface Intent {
        /**
         * Change type of barcode.
         *
         * @param barcodeType
         */
        class SetBarcodeType(val barcodeType: BarcodeType) : Intent

        /**
         * Change count of barcodes.
         *
         * @param barcodeCount
         */
        class SetBarcodeCount(val barcodeCount: Int) : Intent

        /**
         * Start generating barcode list.
         */
        object GenerateBarcodesList : Intent

        /**
         * Approve barcode list.
         */
        object ApproveBarcodeList : Intent
    }

    /**
     * Contract for feature actions.
     */
    sealed interface Action {
        /**
         * Execute change type of barcode.
         *
         * @param barcodeType
         */
        class ExecuteSetBarcodeType(val barcodeType: BarcodeType) : Action

        /**
         * Execute change count of barcodes.
         *
         * @param barcodeCount
         */
        class ExecuteSetBarcodeCount(val barcodeCount: Int) : Action

        /**
         * Execute generate barcode list.
         */
        object ExecuteGenerateBarcodeList : Action

        /**
         * Execute approve barcode list.
         */
        object ExecuteApproveList : Action
    }

    /**
     * Contract for feature effects.
     *
     * @author IosephKnecht
     */
    sealed interface Effect {
        /**
         * Effect for mark is a change type of barcode.
         */
        class ChangeBarcodeType(val barcodeType: BarcodeType) : Effect

        /**
         * Effect for mark is an error when changing count.
         */
        object FailureConstraintBarcodeCount : Effect

        /**
         * Effect for mark is a change count of barcode list.
         */
        class ChangeBarcodeCount(val barcodeCount: Int) : Effect

        /**
         * Effect for mark is an approved barcode list.
         */
        object ApproveBarcodeList : Effect

        /**
         * Effect for mark is a start generating barcode list.
         */
        object StartGenerateBarcodeList : Effect

        /**
         * Effect for mark is an error when generating barcode list.
         *
         * @param throwable
         */
        class FailureGenerateBarcodeList(val throwable: Throwable) : Effect

        /**
         * Effect for mark is a successful generating barcode list.
         *
         * @param barcodeList
         */
        class SuccessGenerateBarcodeList(val barcodeList: BarcodeList) : Effect
    }

    /**
     * Contract for feature news.
     */
    sealed interface News {
        /**
         * Violation of the limit when changing barcode count.
         */
        object FailureConstraintBarcodeCount : News

        /**
         * Error while generating barcode list.
         *
         * @param throwable
         */
        class FailureGenerateBarcodeList(val throwable: Throwable) : News

        /**
         * Barcode list successful approved.
         *
         * @param barcodeList
         */
        class ApproveBarcodeList(val barcodeList: BarcodeList?) : News
    }

    /**
     * Container of generated barcodes.
     *
     * @param barcodeType type of barcode.
     * @param list list of barcodes.
     *
     * @author IosephKnecht
     */
    data class BarcodeList(
        val barcodeType: BarcodeType,
        val list: List<String>
    )
}