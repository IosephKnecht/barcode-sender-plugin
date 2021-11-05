package com.project.iosephknecht.barcode_sender_plugin.presentation.features.devices_selector

import com.android.ddmlib.IDevice
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.EffectProducer
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.StateReducer
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.Store

/**
 * Contract for elements store for multiple choice devices.
 *
 * @author IosephKnecht
 */
internal interface DevicesSelectorFeatureContract {

    /**
     * Feature contract for multiple choice devices.
     *
     * @author IosephKnecht
     */
    interface Feature : Store<State, Intent, Nothing>

    /**
     * Reducer contract for multiple choice devices.
     *
     * @author IosephKnecht
     */
    interface Reducer : StateReducer<State, Effect>

    /**
     * Producer contract for multiple choice devices.
     *
     * @author IosephKnecht
     */
    interface Producer : EffectProducer<State, Intent, Effect>

    /**
     * Bootstrapper contract for multiple choice devices.
     *
     * @author IosephKnecht
     */
    interface Bootstrapper : com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.Bootstrapper<State, Intent>

    /**
     * State for multiple choice devices.
     *
     * @param devices set of choice devices.
     *
     * @author IosephKnecht
     */
    data class State(val devices: Set<IDevice>)

    /**
     * Contract for user intents.
     *
     * @author IosephKnecht
     */
    sealed class Intent(val devices: Array<out IDevice>) {
        class SetSelection(vararg devices: IDevice) : Intent(devices)
        class ResetSelection(vararg devices: IDevice) : Intent(devices)
    }

    /**
     * Contract for effects from producer.
     *
     * @author IosephKnecht
     */
    sealed interface Effect {
        /**
         * Effect for add devices to state.
         */
        class AddSelection(val devices: Set<IDevice>) : Effect

        /**
         * Effect for reset devices selection from state.
         */
        class ResetSelection(val devices: Set<IDevice>) : Effect
    }
}