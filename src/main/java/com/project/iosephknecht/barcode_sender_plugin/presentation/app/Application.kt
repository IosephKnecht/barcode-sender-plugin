package com.project.iosephknecht.barcode_sender_plugin.presentation.app

import com.intellij.ide.AppLifecycleListener
import com.intellij.ide.ApplicationInitializedListener
import com.intellij.openapi.application.ApplicationManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking

/**
 * Implementation [AppLifecycleListener].
 *
 * @author IosephKnecht
 */
@Suppress("UnstableApiUsage")
internal object Application : ApplicationInitializedListener {

    lateinit var applicationDiComponent: ApplicationDiComponent
        private set

    override suspend fun execute(asyncScope: CoroutineScope) {
        applicationDiComponent = DaggerApplicationDiComponent.factory()
            .create(ApplicationManager.getApplication())
    }
}