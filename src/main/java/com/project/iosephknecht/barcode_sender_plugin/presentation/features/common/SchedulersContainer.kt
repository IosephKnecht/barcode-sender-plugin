package com.project.iosephknecht.barcode_sender_plugin.presentation.features.common

import hu.akarnokd.rxjava3.swing.SwingSchedulers
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
    override val computation: SchedulerProvider,
    override val io: SchedulerProvider,
    override val main: SchedulerProvider
) : SchedulersContainer

/**
 * Extension - value for get Swing [SchedulersContainer].
 *
 * @author IosephKnecht
 */
internal val SchedulersContainer.Companion.Swing: SchedulersContainer
    get() = DefaultSchedulersContainer(
        computation = { Schedulers.computation() },
        io = { Schedulers.io() },
        main = { SwingSchedulers.edt() }
    )