package com.project.iosephknecht.barcode_sender_plugin.presentation.features.devices_selector

import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.DefaultStore
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.Logger
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.Store
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.devices_selector.DevicesSelectorFeatureContract.*

/**
 * Implementation for [Feature].
 *
 * @param reducer
 * @param producer
 * @param bootstrapper
 *
 * @author IosephKnecht
 */
internal class DevicesSelectorFeatureDefault(
    reducer: Reducer,
    producer: Producer,
    bootstrapper: Bootstrapper,
    logger: Logger
) : Feature,
    Store<State, Intent, Nothing> by DefaultStore(
        initialState = State(emptySet()),
        reducer = reducer,
        producer = producer,
        bootstrapper = bootstrapper,
        actionMapper = { _, intent -> intent },
        logger = logger
    )