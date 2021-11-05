package com.project.iosephknecht.barcode_sender_plugin.presentation.settings.component

import io.reactivex.rxjava3.core.Observable

/**
 * Contract for component bind feature with UI implementation.
 *
 * @author IosephKnecht
 */
internal interface SettingsComponent {

    /**
     * Observable for state.
     */
    val states: Observable<State>

    /**
     * Observable for events.
     */
    val events: Observable<Event>

    /** @SelfDocumented **/
    val state: State?

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
    fun release()

    /**
     * Contract for view's events.
     */
    interface View {
        val resetSettings: Observable<Unit>
        val applySettings: Observable<Unit>
        val barcodeReceiverKey: Observable<String>
    }

    /**
     * State of component.
     *
     * @param isProcessInitialize component is still being initialized.
     * @param isProcessSaveChanges component is still being saved settings.
     * @param isFailureInitialize an error occurred while initializing the component.
     * @param isChanged settings are changed.
     * @param barcodeReceiveKey barcode receiver key.
     *
     * @author IosephKnecht
     */
    data class State(
        val isProcessInitialize: Boolean,
        val isProcessSaveChanges: Boolean,
        val isFailureInitialize: Boolean,
        val isChanged: Boolean,
        val barcodeReceiveKey: String?
    )

    /**
     * Contract for component events.
     */
    sealed interface Event
}