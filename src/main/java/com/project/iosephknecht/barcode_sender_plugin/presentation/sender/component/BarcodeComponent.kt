package com.project.iosephknecht.barcode_sender_plugin.presentation.sender.component

import com.android.ddmlib.IDevice
import com.project.iosephknecht.barcode_sender_plugin.data.BarcodeType
import io.reactivex.rxjava3.core.Observable

/**
 * Contract for component bind feature with UI implementation.
 *
 * @author IosephKnecht
 */
internal interface BarcodeComponent {

    /**
     * Observable for state.
     */
    val state: Observable<State>

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
         * Event for change selected device.
         */
        val changeSelectedItem: Observable<DeviceItem.Device>

        /**
         * Event for multiple choice device.
         */
        val multipleChoiceItem: Observable<DeviceItem.MultipleChoiceItem>

        /**
         * Event for send barcode.
         */
        val sendBarcode: Observable<Unit>

        /**
         * Event for reset selected item.
         */
        val resetSelected: Observable<DeviceItem.EmptyItem>

        /**
         * Event for change barcode.
         */
        val changeBarcode: Observable<String>

        /**
         * Event for success multiple choice devices.
         */
        val successMultipleChoice: Observable<Set<IDevice>>

        /**
         * Event for interrupt choice devices.
         */
        val interruptMultipleChoice: Observable<Unit>

        /**
         * Event for show barcode type list for generate.
         */
        val showBarcodeGenerateMenu: Observable<Point>

        /**
         * Event for generate barcode.
         */
        val choiceGenerateBarcodeItem: Observable<String>

        /**
         * Event for insert multiple barcode list.
         */
        val insertMultipleBarcodeList: Observable<Pair<BarcodeType, List<String>>>
    }

    /**
     * State of component.
     *
     * @param items list of devices.
     * @param selectedItem current selected device.
     * @param isAvailableSendBarcode available send barcode now.
     * @param barcode current barcode.
     * @param isProcessInitialize component is still being initialized.
     * @param isInitializedError an error occurred while initializing the component.
     *
     * @author IosephKnecht
     */
    data class State(
        val items: List<DeviceItem>,
        val selectedItem: DeviceItem?,
        val barcode: String?,
        val isAvailableSendBarcode: Boolean,
        val isProcessInitialize: Boolean,
        val isInitializedError: Boolean,
        val errorMessage: String?
    )

    /**
     * Contract for ViewModel of items.
     *
     * @author IosephKnecht
     */
    sealed interface DeviceItem {
        /**
         * Item for reset selection from device.
         *
         * @param text title for item.
         */
        data class EmptyItem(val text: String) : DeviceItem

        /**
         * Item for device.
         *
         * @param name device name.
         * @param device
         */
        data class Device(
            val name: String,
            val device: IDevice
        ) : DeviceItem

        /**
         * Item for choice multiple devices.
         *
         * @param text
         */
        data class MultipleChoiceItem(val text: String) : DeviceItem
    }

    /**
     * Contract for ViewModel of barcode generate item.
     */
    sealed interface BarcodeGenerateItem {
        val title: String
        val actionCode: String

        class BarcodeType(
            override val title: String,
            override val actionCode: String
        ) : BarcodeGenerateItem

        class MultipleGenerateBarcodeItem(
            override val title: String,
            override val actionCode: String
        ) : BarcodeGenerateItem
    }

    /**
     * Point of click mouse.
     */
    data class Point(val x: Int, val y: Int)

    /**
     * Contract for component's event.
     *
     * @author IosephKnecht
     */
    sealed interface Event {
        /**
         * Event for show error message after failed sending code operation.
         */
        class ShowErrorSendCode(val throwable: Throwable) : Event

        /**
         * Event for show multiple choice devices dialog.
         */
        object ShowMultipleChoice : Event

        /**
         * Event for show barcode type list.
         */
        class ShowBarcodeTypeList(
            val point: Point,
            val list: List<BarcodeGenerateItem>
        ) : Event

        /**
         * Event for show multiple generate barcode dialog.
         */
        object ShowMultipleGenerateBarcodeDialog : Event

        /**
         * Event for close component after successfully sending code operation.
         */
        object CloseDialog : Event
    }
}