@file:Suppress("unused")

package com.project.iosephknecht.barcode_sender_plugin.presentation.features.common

import io.reactivex.rxjava3.schedulers.Schedulers

private val testTrampoline by lazy {
    DefaultSchedulersContainer(
        computation = { Schedulers.trampoline() },
        io = { Schedulers.trampoline() },
        main = { Schedulers.trampoline() }
    )
}

@Suppress("TestFunctionName")
internal fun SchedulersContainer.Companion.TestTrampoline(): SchedulersContainer = testTrampoline

@Suppress("TestFunctionName")
internal fun SchedulersContainer.Companion.Test(
    computation: SchedulerProvider,
    io: SchedulerProvider,
    main: SchedulerProvider
): SchedulersContainer = DefaultSchedulersContainer(
    computation = computation,
    io = io,
    main = main
)