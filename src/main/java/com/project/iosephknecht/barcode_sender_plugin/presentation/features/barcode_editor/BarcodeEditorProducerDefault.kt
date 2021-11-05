package com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_editor

import com.project.iosephknecht.barcode_sender_plugin.domain.LocalStorage
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.ConfigurableStateOwner
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.DefaultConfigurableStateOwner
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_editor.BarcodeEditorFeatureContract.*
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

/**
 * Default implementation for [BarcodeEditorFeatureContract.Producer].
 *
 * @param localStorage
 *
 * @author IosephKnecht
 */
internal class BarcodeEditorProducerDefault(
    private val localStorage: LocalStorage
) : Producer,
    ConfigurableStateOwner<State> by DefaultConfigurableStateOwner() {

    override fun produce(action: Intent): Observable<Effect> {
        return when (action) {
            Intent.Initialize -> Single.fromCallable { localStorage.getLatestCode().orEmpty() }
                .map<Effect> { Effect.SuccessInitialize(it) }
                .onErrorReturn { Effect.FailureInitialize(it) }
                .toObservable()
            is Intent.EditText -> Observable.fromCallable { localStorage.setLatestCode(action.text) }
                .map<Effect> { Effect.SuccessEdit(action.text) }
                .onErrorReturn { Effect.FailureEdit(it) }
            is Intent.AddText -> Maybe.fromCallable<State.Initialized> { state as? State.Initialized }
                .map { state ->
                    val builder = StringBuilder(state.barcodeText)

                    if (builder.isNotBlank()) {
                        builder.append(System.lineSeparator())
                    }

                    builder.append(action.text).toString()
                }
                .map<Effect> { Effect.SuccessAddText(it) }
                .onErrorReturn { Effect.FailureInitialize(it) }
                .toObservable()
        }
    }
}