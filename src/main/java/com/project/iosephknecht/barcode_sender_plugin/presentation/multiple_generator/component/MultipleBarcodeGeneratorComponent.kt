package com.project.iosephknecht.barcode_sender_plugin.presentation.multiple_generator.component

import com.project.iosephknecht.barcode_sender_plugin.data.BarcodeType
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_generator_multiple.BarcodeGeneratorMultipleFeatureContract
import io.reactivex.rxjava3.core.Observable

/**
 * Contract for component bind feature with UI implementation.
 *
 * @author IosephKnecht
 */
internal interface MultipleBarcodeGeneratorComponent {

    /**
     * Observable of states.
     */
    val state: Observable<State>

    /**
     * Observable of events.
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
     * State of component.
     *
     * @param barcodeCount count of barcodes to generate.
     * @param minCount min count of barcodes to generate.
     * @param maxCount max count of barcodes to generate.
     * @param barcodeType type of barcodes to generate.
     * @param barcodeText text of barcodes.
     * @param isHaveBarcodeList contains already generated barcodes.
     * @param isProcessGenerate process generating barcodes.
     *
     * @author IosephKnecht
     */
    data class State(
        val barcodeCount: Int,
        val minCount: Int,
        val maxCount: Int,
        val barcodeType: BarcodeType,
        val barcodeText: String,
        val isHaveBarcodeList: Boolean,
        val isProcessGenerate: Boolean
    )

    /**
     * Contract for view's events.
     */
    interface View {
        /**
         * Observable of change type of barcodes.
         */
        val barcodeTypeObservable: Observable<BarcodeType>

        /**
         * Observable of change barcodes count.
         */
        val countObservable: Observable<Int>

        /**
         * Observable of generate barcodes action.
         */
        val generateBarcodeList: Observable<Unit>

        /**
         * Observable of approve barcodes action.
         */
        val approveList: Observable<Unit>

        /**
         * Observable of close window action.
         */
        val closeObservable: Observable<Unit>
    }

    /**
     * Contract for component's event.
     *
     * @author IosephKnecht
     */
    sealed interface Event {
        /**
         * Event of approved action.
         *
         * @param barcodeList
         */
        class ApproveBarcodeList(val barcodeList: BarcodeGeneratorMultipleFeatureContract.BarcodeList) : Event

        /**
         * Event of cancel action.
         */
        object CancelBarcodeList : Event
    }
}
