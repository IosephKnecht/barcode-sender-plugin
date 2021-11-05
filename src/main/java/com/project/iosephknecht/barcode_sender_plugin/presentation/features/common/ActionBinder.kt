package com.project.iosephknecht.barcode_sender_plugin.presentation.features.common

import kotlin.reflect.KClass

/**
 * Contract for bind actions.
 *
 * Example for usage:
 * ```
 *  ActionBinder<SomeAction> {
 *      switchMap(SomeAction.TestAction1::class.java)
 *      flatMap(SomeAction.TestAction2:class.java)
 *  }
 * ```
 *
 * @see DefaultStore
 *
 * @author IosephKnecht
 */
internal fun interface ActionBinder<Action> {

    fun ActionBinderScope<Action>.bind()

    /**
     * Extension point for functions.
     *
     * @see bindAllSwitchMap
     * @see bindAllFlatMap
     */
    companion object
}

/**
 * Extension function for bind all action by [Class] by using [ActionBinderScope.switchMap].
 * If class is sealed bind all sealed subclasses, else
 * bind original [Class]
 */
@Suppress("UNUSED")
internal inline fun <reified Action : Any> ActionBinder.Companion.bindAllSwitchMap() = bindAll<Action> { switchMap(it) }

/**
 * Extension function for bind all action by [Class] by using [ActionBinderScope.flatMap].
 * If class is sealed bind all sealed subclasses, else
 * bind original [Class]
 */
@Suppress("UNUSED")
internal inline fun <reified Action : Any> ActionBinder.Companion.bindAllFlatMap() = bindAll<Action> { flatMap(it) }

private inline fun <reified Action : Any> bindAll(crossinline block: ActionBinderScope<Action>.(clazz: Class<out Action>) -> Unit): ActionBinder<Action> {
    val clazz = Action::class

    val clazzList = when (clazz.isSealed) {
        true -> clazz.sealedSubclasses
            .takeUnless(List<KClass<out Action>>::isEmpty)
            ?.asSequence()
            ?: sequenceOf(clazz)
        false -> sequenceOf(clazz)
    }

    return ActionBinder { clazzList.map(KClass<out Action>::java).forEach { block.invoke(this, it) } }
}