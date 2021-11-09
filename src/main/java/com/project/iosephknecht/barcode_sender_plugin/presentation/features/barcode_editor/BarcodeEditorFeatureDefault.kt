package com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_editor

import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_editor.BarcodeEditorFeatureContract.Reducer
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_editor.BarcodeEditorFeatureContract.Producer
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_editor.BarcodeEditorFeatureContract.Bootstrapper
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_editor.BarcodeEditorFeatureContract.State
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_editor.BarcodeEditorFeatureContract.Intent
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.*
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.ActionBinder
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.DefaultStore
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.Logger
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.SchedulersContainer
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.Store
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.bindAllSwitchMap

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
    private val logger: Logger,
    private val schedulersContainer: SchedulersContainer
) : BarcodeEditorFeatureContract.Feature,
    Store<State, Intent, Nothing> by DefaultStore(
        initialState = State.NotInitialized,
        reducer = reducer,
        producer = producer,
        actionMapper = { _, intent -> intent },
        bootstrapper = bootstrapper,
        actionBinder = ActionBinder.bindAllSwitchMap(),
        logger = logger,
        schedulersContainer = schedulersContainer
    )