package com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_editor

import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.ActionBinder
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.DefaultStore
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.Logger
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.Store
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.bindAllSwitchMap
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_editor.BarcodeEditorFeatureContract.Reducer
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_editor.BarcodeEditorFeatureContract.Producer
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_editor.BarcodeEditorFeatureContract.Bootstrapper
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_editor.BarcodeEditorFeatureContract.State
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_editor.BarcodeEditorFeatureContract.Intent

/**
 * Default implementation for [BarcodeEditorFeatureContract.Feature].
 *
 * @param reducer state reducer
 * @param producer effect producer
 *
 * @author IosephKnecht
 */
internal class BarcodeEditorFeatureDefault(
    private val reducer: Reducer,
    private val producer: Producer,
    private val bootstrapper: Bootstrapper,
    private val logger: Logger
) : BarcodeEditorFeatureContract.Feature,
    Store<State, Intent, Nothing> by DefaultStore(
        initialState = State.NotInitialized,
        producer = producer,
        reducer = reducer,
        bootstrapper = bootstrapper,
        actionMapper = { _, intent -> intent },
        actionBinder = ActionBinder.bindAllSwitchMap(),
        logger = logger
    )