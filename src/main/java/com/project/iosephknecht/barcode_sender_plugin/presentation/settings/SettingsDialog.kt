package com.project.iosephknecht.barcode_sender_plugin.presentation.settings

import com.intellij.openapi.Disposable
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.rd.defineNestedLifetime
import com.intellij.openapi.util.Disposer
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import com.project.iosephknecht.barcode_sender_plugin.presentation.app.Application
import com.project.iosephknecht.barcode_sender_plugin.presentation.settings.component.SettingsComponent
import com.project.iosephknecht.barcode_sender_plugin.presentation.settings.di.DaggerSettingsDiComponent
import hu.akarnokd.rxjava3.swing.SwingObservable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject
import javax.swing.JComponent
import javax.swing.JPanel

/**
 * Configurable for [SettingsComponent].
 *
 * @author IosephKnecht
 */
internal class SettingsDialog : Configurable, SettingsComponent.View {

    private var barcodeReceiverKeyTextField: JBTextField? = JBTextField()

    @set:Inject
    internal lateinit var settingsComponent: SettingsComponent

    private val applySubject = PublishSubject.create<Unit>()
    private val resetSubject = PublishSubject.create<Unit>()

    private val logger = logger<SettingsDialog>()

    private val disposable = Disposable { dispose() }

    private val disposables = io.reactivex.rxjava3.disposables.CompositeDisposable()

    init {
        Disposer.register(Disposer.get("ui"), disposable)

        disposable.defineNestedLifetime()
            .bracket(
                opening = {
                    DaggerSettingsDiComponent.factory()
                        .create(Application.applicationDiComponent)
                        .inject(this)

                    settingsComponent.states
                        .subscribe(
                            { state ->
                                barcodeReceiverKeyTextField?.run {
                                    if (text != state.barcodeReceiveKey) {
                                        text = state.barcodeReceiveKey
                                    }
                                }
                            },
                            logger::error
                        )
                        .let(disposables::add)

                    settingsComponent.bindView(this)
                },
                terminationAction = {
                    settingsComponent.unbindView()
                    settingsComponent.release()
                }
            )
    }

    override val resetSettings: Observable<Unit>
        get() = resetSubject
    override val applySettings: Observable<Unit>
        get() = applySubject
    override val barcodeReceiverKey: Observable<String>
        get() = SwingObservable.document(barcodeReceiverKeyTextField!!)
            .map { documentEvent -> documentEvent.document.run { getText(0, length) }.orEmpty() }

    override fun createComponent(): JComponent {
        return FormBuilder.createFormBuilder()
            .addLabeledComponent(JBLabel("Enter receiver key: "), barcodeReceiverKeyTextField!!)
            .addComponentFillVertically(JPanel(), 0)
            .panel
    }

    override fun isModified(): Boolean {
        return settingsComponent.state?.isChanged ?: false
    }

    override fun apply() {
        applySubject.onNext(Unit)
    }

    override fun reset() {
        resetSubject.onNext(Unit)
    }

    override fun cancel() {
        Disposer.dispose(disposable)
    }

    override fun disposeUIResources() {
        Disposer.dispose(disposable)
    }

    override fun getDisplayName(): String {
        return "Barcode Sender Settings Plugin"
    }

    private fun dispose() {
        barcodeReceiverKeyTextField = null
    }
}