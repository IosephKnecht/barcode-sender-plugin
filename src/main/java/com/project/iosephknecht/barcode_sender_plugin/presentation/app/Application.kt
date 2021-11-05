package com.project.iosephknecht.barcode_sender_plugin.presentation.app

import com.intellij.ide.AppLifecycleListener
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project

/**
 * Implementation [AppLifecycleListener].
 *
 * @author IosephKnecht
 */
internal object Application : AppLifecycleListener {

    lateinit var applicationDiComponent: ApplicationDiComponent

    override fun appStarting(projectFromCommandLine: Project?) {
        super.appStarting(projectFromCommandLine)

        applicationDiComponent = DaggerApplicationDiComponent.factory()
            .create(ApplicationManager.getApplication())
    }
}