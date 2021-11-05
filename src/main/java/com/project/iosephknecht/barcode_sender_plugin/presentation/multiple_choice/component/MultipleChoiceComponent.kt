package com.project.iosephknecht.barcode_sender_plugin.presentation.multiple_choice.component

import com.android.ddmlib.IDevice
import io.reactivex.rxjava3.core.Observable

/**
 * Contract for component bind feature multiple choice device with UI implementation.
 *
 * @author IosephKnecht
 */
internal interface MultipleChoiceComponent {

    /**
     * Observable for state.
     */
    val states: Observable<State>

    /**
     * Observable for events.
     */
    val events: Observable<Event>

    /**
     * Execute bind view's events to component.
     */
    fun bindView(view: View)

    /**
     * Execute unbind view's event to component.
     */
    fun unbindView()

    /**
     * Execute release resources of component.
     */
    fun dispose()

    /**
     * Contract for view's events.
     */
    interface View {
        /**
         * Event for select device.
         */
        val selectDeviceObservable: Observable<Device>

        /**
         * Event for reset selection of device.
         */
        val resetSelectDeviceObservable: Observable<Device>

        /**
         * Event for confirm multiple choice.
         */
        val successChoiceObservable: Observable<Unit>

        /**
         * Event for interrupt multiple choice.
         */
        val interruptChoiceObservable: Observable<Unit>
    }

    /**
     * ViewModel for device with choice.
     *
     * @param device devices model.
     * @param isSelected true - if device have selection,
     *                   false -> if device not have selection.
     *
     * @author IosephKnecht
     */
    data class Device(
        val device: IDevice,
        val isSelected: Boolean
    )

    /**
     * State of component.
     *
     * @param deviceList list of devices.
     * @param selectedDeviceCount count of selected devices.
     */
    data class State(
        val deviceList: List<Device>,
        val selectedDeviceCount: Int
    )

    /**
     * Events of component.
     */
    sealed interface Event {
        /**
         * Success choice.
         *
         * @param devices list of selected devices.
         */
        class SuccessChoice(val devices: Set<IDevice>) : Event

        /**
         * Interrupt choice.
         */
        object InterruptChoice : Event
    }
}