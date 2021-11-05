package com.project.iosephknecht.barcode_sender_plugin.presentation.multiple_generator.view

import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.rd.defineNestedLifetime
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.JBIntSpinner
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.JBTextArea
import com.intellij.ui.components.panels.HorizontalLayout
import com.intellij.ui.components.panels.VerticalLayout
import com.project.iosephknecht.barcode_sender_plugin.data.BarcodeType
import com.project.iosephknecht.barcode_sender_plugin.domain.StringProvider
import com.project.iosephknecht.barcode_sender_plugin.presentation.app.Application
import com.project.iosephknecht.barcode_sender_plugin.presentation.multiple_generator.component.MultipleBarcodeGeneratorComponent
import com.project.iosephknecht.barcode_sender_plugin.presentation.multiple_generator.di.DaggerMultipleBarcodeGeneratorDiComponent
import com.project.iosephknecht.barcode_sender_plugin.presentation.ui.DelayedProgressBar
import com.project.iosephknecht.barcode_sender_plugin.presentation.ui.delaySetVisibility
import hu.akarnokd.rxjava3.swing.SwingObservable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.PublishSubject
import java.awt.AWTEvent
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Insets
import java.awt.event.ItemEvent
import java.awt.event.WindowEvent.WINDOW_CLOSING
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JScrollPane

/**
 * Dialog for multiple generate barcode.
 *
 * @param listener
 *
 * @author IosephKnecht
 */
internal class MultipleGeneratorDialog(
    private val listener: MultipleGeneratorListener
) : DialogWrapper(true),
    MultipleBarcodeGeneratorComponent.View {

    private lateinit var barcodeTypeComboBox: ComboBox<BarcodeType>
    private lateinit var barcodeCountSpinner: JBIntSpinner
    private lateinit var barcodeTextArea: JBTextArea
    private lateinit var barcodeGenerateProgressBar: DelayedProgressBar

    private val logger by lazy { logger<MultipleGeneratorDialog>() }
    private val generateSubject = PublishSubject.create<Unit>()
    private val cancelSubject = PublishSubject.create<AWTEvent>()

    init {
        init()
    }

    override val barcodeTypeObservable: Observable<BarcodeType>
        get() = SwingObservable.itemSelection(barcodeTypeComboBox)
            .filter { event -> event.stateChange == ItemEvent.SELECTED && event.item is BarcodeType }
            .map { event -> (event.item as BarcodeType) }

    override val countObservable: Observable<Int>
        get() = SwingObservable.change(barcodeCountSpinner)
            .map { event ->
                val spinner = event.source as JBIntSpinner
                spinner.number
            }

    override val generateBarcodeList: Observable<Unit>
        get() = generateSubject

    override val approveList: Observable<Unit>
        get() = cancelSubject
            .filter { awtEvent -> awtEvent.id != WINDOW_CLOSING }
            .map { }

    override val closeObservable: Observable<Unit>
        get() = cancelSubject
            .filter { awtEvent -> awtEvent.id == WINDOW_CLOSING }
            .map {  }

    override fun init() {
        this.title = StringProvider.PlainString.PLUGIN_NAME.value

        val barcodeTypeCellRenderer = org.jdesktop.swingx.renderer.DefaultListRenderer { item ->
            when (item) {
                is BarcodeType -> item.title
                else -> null
            }
        }

        barcodeTypeComboBox = ComboBox(BarcodeType.values()).apply { renderer = barcodeTypeCellRenderer }
        barcodeCountSpinner = JBIntSpinner(1, 1, 100, 1)
        barcodeTextArea = JBTextArea()
        barcodeGenerateProgressBar = DelayedProgressBar("Process generate...").apply { isVisible = false }

        setOKButtonText("Generate")

        val component = DaggerMultipleBarcodeGeneratorDiComponent.factory()
            .create(Application.applicationDiComponent)
            .component

        val componentDisposable = CompositeDisposable()

        disposable.defineNestedLifetime()
            .bracket(
                opening = {
                    component.events
                        .subscribe(
                            { event ->
                                when (event) {
                                    is MultipleBarcodeGeneratorComponent.Event.ApproveBarcodeList -> {
                                        event.barcodeList.let { (type, list) ->
                                            listener.approve(
                                                barcodeType = type,
                                                list = list
                                            )

                                            super.doCancelAction()
                                        }
                                    }
                                    MultipleBarcodeGeneratorComponent.Event.CancelBarcodeList -> {
                                        listener.cancel()
                                        super.doCancelAction()
                                    }
                                }
                            },
                            logger::error
                        )
                        .let(componentDisposable::add)

                    component.state
                        .subscribe(
                            { state ->
                                barcodeTypeComboBox.selectedItem = state.barcodeType
                                barcodeCountSpinner.model.value = state.barcodeCount
                                okAction.isEnabled = !state.isProcessGenerate
                                barcodeGenerateProgressBar.delaySetVisibility(state.isProcessGenerate)

                                setCancelButtonText(
                                    when (state.isProcessGenerate) {
                                        true -> "Cancel"
                                        false -> "OK"
                                    }
                                )

                                with(barcodeTextArea) { if (text != state.barcodeText) text = state.barcodeText }
                            },
                            logger::error
                        )
                        .let(componentDisposable::add)

                    component.bindView(this)
                },
                terminationAction = {
                    componentDisposable.clear()
                    component.unbindView()
                    component.dispose()
                }
            )

        super.init()
    }

    override fun createCenterPanel(): JComponent {
        val rootPanel = JPanel(BorderLayout())
        val northColumn = JPanel(VerticalLayout(10))

        val firstNorthRow = JPanel(HorizontalLayout(10))

        firstNorthRow.add(JBLabel("Choice type"), HorizontalLayout.LEFT)
        firstNorthRow.add(barcodeTypeComboBox, HorizontalLayout.LEFT)
        firstNorthRow.add(JBLabel("Choice count"), HorizontalLayout.RIGHT)
        firstNorthRow.add(barcodeCountSpinner, HorizontalLayout.RIGHT)

        northColumn.add(firstNorthRow)
        northColumn.add(barcodeGenerateProgressBar)

        rootPanel.add(northColumn, BorderLayout.NORTH)

        barcodeTextArea = JBTextArea().apply {
            lineWrap = true
            wrapStyleWord = true
            margin = Insets(12, 12, 12, 12)
            isEditable = false
        }

        val scrollPanel = JBScrollPane(
            barcodeTextArea,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        ).apply {
            preferredSize = Dimension(100, 100)
        }

        rootPanel.add(scrollPanel, BorderLayout.CENTER)

        return rootPanel
    }

    override fun doOKAction() {
        generateSubject.onNext(Unit)
    }

    override fun doCancelAction(source: AWTEvent) {
        cancelSubject.onNext(source)
    }
}