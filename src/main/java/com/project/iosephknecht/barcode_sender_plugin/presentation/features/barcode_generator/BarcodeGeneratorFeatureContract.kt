package com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_generator

import com.project.iosephknecht.barcode_sender_plugin.data.BarcodeType
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.EffectProducer
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.StateReducer
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.Store

/**
 * Contract for barcode generator feature.
 *
 * @author IosephKnecht
 */
internal interface BarcodeGeneratorFeatureContract {

    /**
     * Contract for store barcode generator.
     *
     * @author IosephKnecht
     */
    interface Feature : Store<State, Intent, News>

    /**
     * Contract for [StateReducer] of barcode generator.
     *
     * @author IosephKnecht
     */
    interface Reducer : StateReducer<State, Effect>

    /**
     * Contract for [EffectProducer] of barcode generator.
     *
     * @author IosephKnecht
     */
    interface Producer : EffectProducer<State, Action, Effect>

    /**
     * Contract for [com.project.iosephknecht.presentation.features.common.ActionMapper] of barcode generator.
     *
     * @author IosephKnecht
     */
    interface ActionMapper : com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.ActionMapper<State, Intent, Action>

    /**
     * Contract for [com.project.iosephknecht.presentation.features.common.NewsMapper] of barcode generator.
     *
     * @author IosephKnecht
     */
    interface NewsMapper : com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.NewsMapper<State, Effect, News>

    /**
     * Contract for state of barcode generator.
     *
     * @author IosephKnecht
     */
    sealed interface State {
        /**
         * Barcode not generating now.
         */
        object Empty : State

        /**
         * Process generate barcode state.
         *
         * @param barcodeType type of generating barcode.
         */
        data class ProcessGenerateBarcode(val barcodeType: BarcodeType) : State
    }

    /**
     * Contract for user wish of barcode generator.
     *
     * @author IosephKnecht
     */
    sealed interface Intent {
        /**
         * User wish for generate barcode.
         *
         * @param barcodeType type of barcode.
         */
        class GenerateBarcode(val barcodeType: BarcodeType) : Intent
    }

    /**
     * Contract for feature action of barcode generator/
     *
     * @author IosephKnecht
     */
    sealed interface Action {
        /**
         * Execute generate barcode by type.
         *
         * @param barcodeType type of barcode.
         */
        class ExecuteGenerateBarcodeType(val barcodeType: BarcodeType) : Action
    }

    /**
     * Contract for produced effect.
     */
    sealed interface Effect {
        /**
         * Effect for mark start generating barcode.
         *
         * @param barcodeType type of barcode.
         */
        class StartGenerateBarcode(val barcodeType: BarcodeType) : Effect

        /**
         * Effect for mark error while generate barcode.
         *
         * @param throwable
         */
        class FailureGenerateBarcode(val throwable: Throwable) : Effect

        /**
         * Effect for mark success generation barcode.
         *
         * @param barcode generated barcode.
         */
        class SuccessGenerateBarcode(val barcode: String) : Effect
    }

    /**
     * Contract for news of barcode generator.
     */
    sealed interface News {

        /**
         * Barcode success generated.
         *
         * @param barcode barcode value.
         * @param barcodeType type of barcode.
         */
        class SuccessfulGenerateBarcode(
            val barcode: String,
            val barcodeType: BarcodeType
        ) : News

        /**
         * Barcode failure generated.
         *
         * @param barcodeType type of barcode.
         * @param throwable
         */
        class FailureGenerateBarcode(
            val barcodeType: BarcodeType,
            val throwable: Throwable
        ) : News
    }
}