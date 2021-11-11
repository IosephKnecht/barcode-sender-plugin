package com.project.iosephknecht.barcode_sender_plugin.presentation.features.common

import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * [Scheduler] container contract.
 *
 * @author IosephKnecht
 */
internal interface SchedulersContainer {

    /**
     * Get bounded [Scheduler] for computation tasks.
     */
    val computation: SchedulerProvider

    /**
     * Get unbounded [Scheduler] for IO tasks.
     */
    val io: SchedulerProvider

    /**
     * Get ui event thread [Scheduler].
     */
    val main: SchedulerProvider

    /**
     * Extension point.
     */
    companion object
}

/**
 * Contract of [Scheduler] provider.
 *
 * @author IosephKnecht
 */
internal fun interface SchedulerProvider {

    /** @SelfDocumented **/
    fun get(): Scheduler
}

/**
 * Default implementation [SchedulersContainer].
 *
 * @param computation
 * @param io
 * @param main
 *
 * @author IosephKnecht
 */
internal class DefaultSchedulersContainer(
    override val computation: SchedulerProvider = SchedulerProvider { Schedulers.computation() },
    override val io: SchedulerProvider = SchedulerProvider { Schedulers.io() },
    override val main: SchedulerProvider
) : SchedulersContainer