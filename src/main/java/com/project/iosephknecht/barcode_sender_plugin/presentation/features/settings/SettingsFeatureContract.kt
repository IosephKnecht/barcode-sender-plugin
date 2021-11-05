package com.project.iosephknecht.barcode_sender_plugin.presentation.features.settings

import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.EffectProducer
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.StateReducer
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.Store

/**
 * Extension - value get current barcode receiver key.
 */
internal val SettingsFeatureContract.State.currentBarcodeReceiverKey: String?
    get() = when (this) {
        SettingsFeatureContract.State.NotInitialize,
        SettingsFeatureContract.State.ProcessInitialize,
        is SettingsFeatureContract.State.FailureInitialize -> null
        is SettingsFeatureContract.State.Data -> barcodeReceiverKey
        is SettingsFeatureContract.State.ChangedSettings -> changedBarcodeReceiverKey
        is SettingsFeatureContract.State.ProcessSaveSettings -> changedBarcodeReceiverKey
    }

/**
 * Extension - value for get flag failure initialize state.
 */
internal val SettingsFeatureContract.State.isFailureInitialize: Boolean
    get() = when (this) {
        SettingsFeatureContract.State.NotInitialize,
        SettingsFeatureContract.State.ProcessInitialize,
        is SettingsFeatureContract.State.Data,
        is SettingsFeatureContract.State.ChangedSettings,
        is SettingsFeatureContract.State.ProcessSaveSettings -> false
        is SettingsFeatureContract.State.FailureInitialize -> true
    }

/**
 * Extension - value for get flag process initialize state.
 */
internal val SettingsFeatureContract.State.isProcessInitialize: Boolean
    get() = when (this) {
        SettingsFeatureContract.State.NotInitialize,
        is SettingsFeatureContract.State.FailureInitialize,
        is SettingsFeatureContract.State.Data,
        is SettingsFeatureContract.State.ChangedSettings,
        is SettingsFeatureContract.State.ProcessSaveSettings -> false
        SettingsFeatureContract.State.ProcessInitialize -> true
    }

/**
 * Extension - value for get flag process save changes state.
 */
internal val SettingsFeatureContract.State.isProcessSaveChanges: Boolean
    get() = when (this) {
        SettingsFeatureContract.State.NotInitialize,
        SettingsFeatureContract.State.ProcessInitialize,
        is SettingsFeatureContract.State.FailureInitialize,
        is SettingsFeatureContract.State.Data,
        is SettingsFeatureContract.State.ChangedSettings -> false
        is SettingsFeatureContract.State.ProcessSaveSettings -> true
    }

/**
 * Extension - value for get flag have changes state.
 */
internal val SettingsFeatureContract.State.isHaveChanges: Boolean
    get() = when (this) {
        SettingsFeatureContract.State.NotInitialize,
        SettingsFeatureContract.State.ProcessInitialize,
        is SettingsFeatureContract.State.FailureInitialize,
        is SettingsFeatureContract.State.Data,
        is SettingsFeatureContract.State.ProcessSaveSettings -> false
        is SettingsFeatureContract.State.ChangedSettings -> true
    }

/**
 * Contract of settings feature.
 *
 * @author IosephKnecht
 */
internal sealed interface SettingsFeatureContract {

    /**
     * Contract of settings feature.
     */
    interface Feature : Store<State, Intent, News>

    /**
     * Contract of settings feature reducer.
     */
    interface Reducer : StateReducer<State, Effect>

    /**
     * Contract of settings feature producer.
     */
    interface Producer : EffectProducer<State, Action, Effect>

    /**
     * Contract of settings feature action mapper.
     */
    interface ActionMapper : com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.ActionMapper<State, Intent, Action>

    /**
     * Contract of settings feature news mapper.
     */
    interface NewsMapper : com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.NewsMapper<State, Effect, News>

    /**
     * Contract of settings feature state.
     */
    sealed interface State {
        /**
         * Not initialized state.
         */
        object NotInitialize : State

        /**
         * Process initialize state.
         */
        object ProcessInitialize : State

        /**
         * Failure initialize state.
         *
         * @param throwable init error.
         */
        data class FailureInitialize(val throwable: Throwable) : State

        /**
         * Success initialized state.
         *
         * @param barcodeReceiverKey key of barcode receiver.
         */
        data class Data(val barcodeReceiverKey: String) : State

        /**
         * Changed settings state.
         *
         * @param originalBarcodeReceiverKey loaded key from settings.
         * @param changedBarcodeReceiverKey changed key.
         */
        data class ChangedSettings(
            val originalBarcodeReceiverKey: String,
            val changedBarcodeReceiverKey: String
        ) : State

        /**
         * Process save changed barcode receiver key.
         *
         * @param originalBarcodeReceiverKey loaded key from settings.
         * @param changedBarcodeReceiverKey changed key.
         */
        data class ProcessSaveSettings(
            val originalBarcodeReceiverKey: String,
            val changedBarcodeReceiverKey: String
        ) : State
    }

    /**
     * Contract for user wishes.
     */
    sealed interface Intent {
        /**
         * Initialize feature.
         */
        object LoadSettings : Intent

        /**
         * Save settings.
         */
        object SaveSettings : Intent

        /**
         * Reset settings.
         */
        object ResetSettings : Intent

        /**
         * Set barcode receiver key.
         *
         * @param key value of key.
         */
        class SetBarcodeReceiveKey(val key: String) : Intent
    }

    /**
     * Contract for feature actions.
     */
    sealed interface Action {
        /**
         * Execute load settings.
         */
        object ExecuteLoadSettings : Action

        /**
         * Execute save settings.
         */
        object ExecuteSaveSettings : Action

        /**
         * Execute reset changed settings.
         */
        object ExecuteResetSettings : Action

        /**
         * Execute set barcode receiver key.
         *
         * @param key value of key.
         */
        class ExecuteSetBarcodeReceiveKey(val key: String) : Action
    }

    /**
     * Contract for feature effects.
     */
    sealed interface Effect {
        /**
         * Start loading settings.
         */
        object StartLoadSettings : Effect

        /**
         * Failure load settings.
         *
         * @param throwable
         */
        class FailureLoadSettings(val throwable: Throwable) : Effect

        /**
         * Success load settings.
         *
         * @param receiverKey barcode receiver key.
         */
        class SuccessLoadSettings(val receiverKey: String) : Effect

        /**
         * Set barcode receiver key.
         *
         * @param key value of key.
         */
        class SetBarcodeReceiveKey(val key: String) : Effect

        /**
         * Start save settings.
         */
        object StartSaveSettings : Effect

        /**
         * Failure save settings.
         *
         * @param throwable
         */
        class FailureSaveSettings(val throwable: Throwable) : Effect

        /**
         * Success save settings.
         *
         * @param receiverKey value of key.
         */
        class SuccessSaveSettings(val receiverKey: String) : Effect

        object ResetSettings : Effect
    }

    /**
     * Contract for feature news.
     */
    sealed interface News {

        /**
         * News about fail save settings.
         *
         * @param throwable
         */
        class FailureSaveSettings(val throwable: Throwable) : News
    }
}