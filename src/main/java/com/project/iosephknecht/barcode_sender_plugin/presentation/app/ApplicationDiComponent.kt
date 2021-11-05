package com.project.iosephknecht.barcode_sender_plugin.presentation.app

import com.intellij.openapi.application.Application
import com.project.iosephknecht.barcode_sender_plugin.domain.*
import com.project.iosephknecht.barcode_sender_plugin.domain.BarcodeGenerator
import com.project.iosephknecht.barcode_sender_plugin.domain.DefaultBarcodeGenerator
import com.project.iosephknecht.barcode_sender_plugin.domain.LocalStorage
import com.project.iosephknecht.barcode_sender_plugin.domain.SettingsStorage
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides

/**
 * Contract for DI - container.
 *
 * @see Application
 *
 * @author IosephKnecht
 */
@ApplicationScope
@Component(modules = [ApplicationDiModule::class])
internal interface ApplicationDiComponent {

    val localStorage: LocalStorage
    val settingsStorage: SettingsStorage
    val barcodeGenerator: BarcodeGenerator
    val stringProvider: StringProvider

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): ApplicationDiComponent
    }
}

/**
 * Module for [ApplicationDiComponent].
 *
 * @author IosephKnecht
 */
@Module
internal class ApplicationDiModule {

    @ApplicationScope
    @Provides
    fun provideLocalStorage(application: Application): LocalStorage {
        return application.getService(LocalStorage::class.java)
    }

    @ApplicationScope
    @Provides
    fun provideSettingsStorage(application: Application): SettingsStorage {
        return application.getService(SettingsStorage::class.java)
    }

    @ApplicationScope
    @Provides
    fun provideBarcodeGenerator(): BarcodeGenerator {
        return DefaultBarcodeGenerator()
    }

    @ApplicationScope
    @Provides
    fun provideStringProvider(): StringProvider {
        return DefaultStringProvider()
    }
}