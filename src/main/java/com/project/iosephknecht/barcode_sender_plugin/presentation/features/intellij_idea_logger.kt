package com.project.iosephknecht.barcode_sender_plugin.presentation.features

import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.Logger
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.Store
import kotlin.reflect.KClass

/**
 * Extension - function for creating a logger for [Store].
 *
 * @param storeKClass
 *
 * @see com.intellij.openapi.diagnostic.Logger
 *
 * @author IosephKnecht
 */
internal fun <T : Store<*, *, *>> Logger.Companion.intellijIdeaLogger(storeKClass: KClass<T>) = object : Logger {

    private val nativeLogger by lazy {
        com.intellij.openapi.diagnostic.Logger.getInstance(storeKClass.java)
    }

    override fun error(throwable: Throwable) {
        nativeLogger.error(throwable)
    }
}