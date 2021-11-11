package com.project.iosephknecht.barcode_sender_plugin.presentation.features.common

import hu.akarnokd.rxjava3.swing.SwingSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * Extension - value for get Swing [SchedulersContainer].
 *
 * @author IosephKnecht
 */
internal val SchedulersContainer.Companion.Swing: SchedulersContainer by lazy {
    DefaultSchedulersContainer(
        computation = { Schedulers.computation() },
        io = { Schedulers.io() },
        main = { SwingSchedulers.edt() }
    )
}