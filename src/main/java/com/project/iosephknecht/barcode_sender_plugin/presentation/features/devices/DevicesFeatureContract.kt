package com.project.iosephknecht.barcode_sender_plugin.presentation.features.devices

import com.android.ddmlib.AndroidDebugBridge
import com.android.ddmlib.IDevice
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.EffectProducer
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.StateReducer
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.Store

/**
 * Extension - value for get [AndroidDebugBridge] from current state.
 *
 * @author IosephKnecht
 */
internal inline val DevicesFeatureContract.State.debugBridge: AndroidDebugBridge?
    get() = when (this) {
        is DevicesFeatureContract.State.Data -> debugBridge
        is DevicesFeatureContract.State.FailureInitialize -> null
        DevicesFeatureContract.State.ProcessInitialize -> null
        is DevicesFeatureContract.State.ProcessLoadDevices -> debugBridge
        DevicesFeatureContract.State.UnInitialize -> null
    }

/**
 * Extension - value for get devices list from current state.
 *
 * @author IosephKnecht
 */
internal inline val DevicesFeatureContract.State.devices: List<DevicesFeatureContract.Device>?
    get() = when (this) {
        DevicesFeatureContract.State.UnInitialize,
        DevicesFeatureContract.State.ProcessInitialize,
        is DevicesFeatureContract.State.FailureInitialize -> null
        is DevicesFeatureContract.State.Data -> devices
        is DevicesFeatureContract.State.ProcessLoadDevices -> devices
    }

/**
 * Contract for Devices feature.
 *
 * @author IosephKnecht
 */
internal interface DevicesFeatureContract {

    interface Feature : Store<State, Intent, News>

    interface Producer : EffectProducer<State, Action, Effect>

    interface Reducer : StateReducer<State, Effect>

    interface Bootstrapper : com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.Bootstrapper<State, Action>

    interface ActionMapper : com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.ActionMapper<State, Intent, Action>

    interface NewsMapper : com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.NewsMapper<State, Effect, News>

    /**
     * Contract for describe devices feature state.
     *
     * @author IosephKnecht
     */
    sealed interface State {

        /**
         * Initial, empty state.
         */
        object UnInitialize : State

        /**
         * Process initialize state.
         * Devices not yet loaded.
         */
        object ProcessInitialize : State

        /**
         * Process initialize failed.
         *
         * @param throwable failure reason
         */
        data class FailureInitialize(val throwable: Throwable) : State

        /**
         * Process update devices list after successfully initialize.
         *
         * @param devices current devices list.
         * @param debugBridge
         */
        data class ProcessLoadDevices(
            val devices: List<Device>,
            internal val debugBridge: AndroidDebugBridge
        ) : State

        /**
         * State after successfully initialize.
         *
         * @param devices current devices list.
         * @param debugBridge
         */
        data class Data(
            val devices: List<Device>,
            internal val debugBridge: AndroidDebugBridge
        ) : State
    }

    /**
     * Contract for user wishes.
     *
     * @author IosephKnecht
     */
    sealed interface Intent {

        /**
         * Reload devices list.
         */
        object Reload : Intent
    }

    /**
     * Contract for actions.
     *
     * @author IosepKnecht
     */
    sealed interface Action {

        /**
         * Execute reload devices list.
         */
        object InvokeReload : Action

        /**
         * Change bridge for get devices list.
         *
         * @param bridge changed bridge.
         */
        class SetDebugBridge(val bridge: AndroidDebugBridge) : Action
    }

    /**
     * Contract for effect produced actions.
     *
     * @author IosephKnecht
     */
    sealed interface Effect {

        /**
         * Effect for mark start process load devices list.
         */
        object StartLoadDevices : Effect

        /**
         * Effect for mark success process load devices list.
         */
        class SuccessLoadDevices(val bridge: AndroidDebugBridge, val devices: List<Device>) : Effect

        /**
         * Effect for mark failure process load devices list.
         */
        class FailureLoadDevices(val throwable: Throwable) : Effect
    }

    /**
     * Contract for news after state changed.
     *
     * @author IosephKnecht
     */
    sealed interface News {
        /**
         * News about device list changed.
         */
        object DeviceListChanged : News
    }

    /**
     * Own implementation device.
     * Is a wrapper over [com.android.ddmlib.IDevice].
     * Override methode [Object.equals] and [Object.hashCode] for correct updates on UI.
     *
     * In the future, it is planned not to focus on the implementation of [com.android.ddmlib.IDevice],
     * replacing this model with a more renounced one.
     *
     * @author IosephKnecht
     */
    class Device(device: IDevice) : IDevice by device {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Device

            return (serialNumber == other.serialNumber)
                .and(state == other.state)
                .and(avdName == other.avdName)
        }

        override fun hashCode(): Int {
            var result = serialNumber?.hashCode() ?: 0
            result = 31 * result + state.hashCode()
            result = 31 * result + (avdName?.hashCode() ?: 0)
            return result
        }

    }
}