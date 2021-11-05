package com.project.iosephknecht.barcode_sender_plugin.presentation.features.common

/**
 * Contract for scope for usage in [ActionBinder].
 *
 * @author IosephKnecht
 */
internal interface ActionBinderScope<Action> {

    /**
     * Bind an action using a [io.reactivex.rxjava3.core.Observable.switchMap]
     */
    fun <T : Action> switchMap(clazz: Class<T>)

    /**
     * Bind an action using a [io.reactivex.rxjava3.core.Observable.flatMap]
     */
    fun <T : Action> flatMap(clazz: Class<T>)
}