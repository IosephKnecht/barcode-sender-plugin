package com.project.iosephknecht.barcode_sender_plugin.presentation.features.devices

import com.project.iosephknecht.barcode_sender_plugin.presentation.features.devices.DevicesFeatureContract.State
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.devices.DevicesFeatureContract.Effect

/**
 * Implementation state reducer for devices list.
 *
 * @author IosephKnecht
 */
internal class DevicesStateReducer : DevicesFeatureContract.Reducer {

    override fun reduce(state: State, effect: Effect): State {
        return when (state) {
            is State.UnInitialize -> state + effect
            is State.ProcessInitialize -> state + effect
            is State.FailureInitialize -> state + effect
            is State.Data -> state + effect
            is State.ProcessLoadDevices -> state + effect
        }
    }

    private operator fun State.UnInitialize.plus(effect: Effect): State {
        return when (effect) {
            Effect.StartLoadDevices -> State.ProcessInitialize
            is Effect.FailureLoadDevices,
            is Effect.SuccessLoadDevices -> this
        }
    }

    private operator fun State.ProcessInitialize.plus(effect: Effect): State {
        return when (effect) {
            Effect.StartLoadDevices -> this
            is Effect.FailureLoadDevices -> State.FailureInitialize(effect.throwable)
            is Effect.SuccessLoadDevices -> State.Data(effect.devices, effect.bridge)
        }
    }

    private operator fun State.FailureInitialize.plus(effect: Effect): State {
        return when (effect) {
            Effect.StartLoadDevices -> State.ProcessInitialize
            is Effect.FailureLoadDevices,
            is Effect.SuccessLoadDevices -> this
        }
    }

    private operator fun State.ProcessLoadDevices.plus(effect: Effect): State {
        return when (effect) {
            Effect.StartLoadDevices -> this
            is Effect.FailureLoadDevices -> this
            is Effect.SuccessLoadDevices -> State.Data(effect.devices, effect.bridge)
        }
    }

    private operator fun State.Data.plus(effect: Effect): State {
        return when (effect) {
            Effect.StartLoadDevices -> State.ProcessLoadDevices(this.devices, this.debugBridge)
            is Effect.FailureLoadDevices -> this
            is Effect.SuccessLoadDevices -> this
        }
    }
}