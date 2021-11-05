package com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_editor

import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.EffectProducer
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.StateReducer
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.Store

/**
 * Contract for barcode editor feature.
 *
 * @author IosephKnecht
 */
internal interface BarcodeEditorFeatureContract {

    interface Feature : Store<State, Intent, Nothing>

    interface Reducer : StateReducer<State, Effect>

    interface Producer : EffectProducer<State, Intent, Effect>

    interface Bootstrapper : com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.Bootstrapper<State, Intent>

    /**
     * Contract for user wishes.
     *
     * @author IosephKnecht
     */
    sealed interface Intent {
        /**
         * User wish for initialize saved code.
         */
        object Initialize : Intent

        /**
         * User wish for change current barcode.
         *
         * @param text changed text.
         */
        class EditText(val text: String) : Intent

        /**
         * User wish for add text last line.
         */
        class AddText(val text: String) : Intent
    }

    /**
     * Contract for barcode editor state.
     *
     * @author IosephKnecht
     */
    sealed interface State {
        /**
         * Not initialize state.
         */
        object NotInitialized : State

        /**
         * Initialized state.
         *
         * @param barcodeText current barcode.
         */
        data class Initialized(val barcodeText: String) : State
    }

    /**
     * Contract for barcode editor feature effects.
     */
    sealed interface Effect {
        /**
         * Effect for mark success initialize.
         *
         * @param initialText restored text
         */
        class SuccessInitialize(val initialText: String) : Effect

        /**
         * Effect for mark failure initialize.
         *
         * @param throwable initialize error
         */
        class FailureInitialize(val throwable: Throwable) : Effect

        /**
         * Effect for mark success change text.
         *
         * @param barcode changed text.
         */
        class SuccessEdit(val barcode: String) : Effect

        /**
         * Effect for mark failed change text.
         *
         * @param throwable change text error.
         */
        class FailureEdit(val throwable: Throwable) : Effect

        /**
         * Effect for mark success concatenate previous text
         * and new text.
         *
         * @param barcode changed text.
         */
        class SuccessAddText(val barcode: String) : Effect
    }
}