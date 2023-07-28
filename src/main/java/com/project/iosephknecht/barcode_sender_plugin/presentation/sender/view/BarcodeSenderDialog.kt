package com.project.iosephknecht.barcode_sender_plugin.presentation.sender.view

import com.android.ddmlib.IDevice
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.rd.defineNestedLifetime
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.IdeBorderFactory
import com.intellij.ui.components.DialogPanel
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.JBTextArea
import com.intellij.util.ui.JBUI
import com.project.iosephknecht.barcode_sender_plugin.data.BarcodeType
import com.project.iosephknecht.barcode_sender_plugin.domain.StringProvider
import com.project.iosephknecht.barcode_sender_plugin.presentation.app.Application
import com.project.iosephknecht.barcode_sender_plugin.presentation.multiple_choice.component.MultipleChoiceListener
import com.project.iosephknecht.barcode_sender_plugin.presentation.multiple_generator.view.MultipleGeneratorDialog
import com.project.iosephknecht.barcode_sender_plugin.presentation.multiple_generator.view.MultipleGeneratorListener
import com.project.iosephknecht.barcode_sender_plugin.presentation.sender.component.BarcodeComponent
import com.project.iosephknecht.barcode_sender_plugin.presentation.sender.component.BarcodeComponent.Event
import com.project.iosephknecht.barcode_sender_plugin.presentation.sender.component.BarcodeComponent.DeviceItem
import com.project.iosephknecht.barcode_sender_plugin.presentation.sender.di.DaggerBarcodeDiComponent
import com.project.iosephknecht.barcode_sender_plugin.presentation.ui.BarcodeSenderIcons
import com.project.iosephknecht.barcode_sender_plugin.presentation.ui.DelayedProgressBar
import com.project.iosephknecht.barcode_sender_plugin.presentation.ui.delaySetVisibility
import hu.akarnokd.rxjava3.swing.SwingObservable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.PublishSubject
import org.jdesktop.swingx.renderer.DefaultListRenderer
import org.jdesktop.swingx.renderer.IconValue
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Insets
import java.awt.event.ItemEvent
import java.awt.event.MouseEvent
import javax.inject.Inject
import javax.swing.*

/**
 * Dialog for [BarcodeComponent].
 *
 * @author IosephKnecht
 */
internal class BarcodeSenderDialog(private val project: Project) : DialogWrapper(project, true),
    BarcodeComponent.View,
    MultipleChoiceListener,
    MultipleGeneratorListener {

    private lateinit var textArea: JTextArea
    private lateinit var textAreaContainer: JComponent
    private lateinit var devicesComboBox: ComboBox<DeviceItem>
    private lateinit var progressBar: DelayedProgressBar
    private lateinit var dialogPanel: DialogPanel
    private lateinit var initialErrorLabel: JLabel

    private val emptyBorder by lazy(LazyThreadSafetyMode.NONE) {
        JBUI.Borders.empty()
    }

    @Suppress("DialogTitleCapitalization")
    private val titleBorder by lazy(LazyThreadSafetyMode.NONE) {
        IdeBorderFactory.createTitledBorder("Please, enter your codes below...", false)
    }

    private val logger = logger<BarcodeSenderDialog>()
    private val viewDisposables = CompositeDisposable()

    @set:Inject
    internal lateinit var component: BarcodeComponent

    @set:Inject
    internal lateinit var multipleChoiceDialogFactory: MultipleChoiceDialogFactory

    private val okActionSubject = PublishSubject.create<Unit>()

    private val insertMultipleBarcodeListSubject = PublishSubject.create<Pair<BarcodeType, List<String>>>()

    override val changeSelectedItem: Observable<DeviceItem.Device>
        get() = SwingObservable.itemSelection(devicesComboBox)
            .filter { event -> event.stateChange == ItemEvent.SELECTED && event.item is DeviceItem.Device }
            .map { event -> (event.item as DeviceItem.Device) }

    override val sendBarcode: Observable<Unit>
        get() = okActionSubject

    override val resetSelected: Observable<DeviceItem.EmptyItem>
        get() = SwingObservable.itemSelection(devicesComboBox)
            .filter { event -> event.stateChange == ItemEvent.SELECTED && event.item is DeviceItem.EmptyItem }
            .map { event -> (event.item as DeviceItem.EmptyItem) }

    override val multipleChoiceItem: Observable<DeviceItem.MultipleChoiceItem>
        get() = SwingObservable.itemSelection(devicesComboBox)
            .filter { event -> event.stateChange == ItemEvent.SELECTED && event.item is DeviceItem.MultipleChoiceItem }
            .map { event -> event.item as DeviceItem.MultipleChoiceItem }

    override val changeBarcode: Observable<String>
        get() = SwingObservable.document(textArea)
            .map { documentEvent -> documentEvent.document.run { getText(0, length) }.orEmpty() }

    override val successMultipleChoice: PublishSubject<Set<IDevice>> = PublishSubject.create()

    override val interruptMultipleChoice: PublishSubject<Unit> = PublishSubject.create()

    override val choiceGenerateBarcodeItem: PublishSubject<String> = PublishSubject.create()

    override val insertMultipleBarcodeList: Observable<Pair<BarcodeType, List<String>>>
        get() = insertMultipleBarcodeListSubject

    override val showBarcodeGenerateMenu: Observable<BarcodeComponent.Point>
        get() = SwingObservable.mouse(textArea)
            .filter { event ->
                with(event) {
                    (id == MouseEvent.MOUSE_CLICKED)
                        .and(button == MouseEvent.BUTTON3)
                        .and(clickCount == 1)
                }
            }
            .map { BarcodeComponent.Point(x = it.x, y = it.y) }


    override fun successChoice(devices: Set<IDevice>) {
        successMultipleChoice.onNext(devices)
    }

    override fun interruptChoice() {
        interruptMultipleChoice.onNext(Unit)
    }

    override fun approve(barcodeType: BarcodeType, list: List<String>) {
        insertMultipleBarcodeListSubject.onNext(barcodeType to list)
    }

    override fun cancel() {
        // ignore
    }

    init {
        DaggerBarcodeDiComponent.factory()
            .create(Application.applicationDiComponent, listener = this)
            .inject(this)

        init()
    }

    override fun init() {
        this.title = StringProvider.PlainString.PLUGIN_NAME.value

        textArea = JBTextArea(10, 20).apply {
            lineWrap = true
            wrapStyleWord = true
            margin = Insets(12, 12, 12, 12)
        }

        devicesComboBox = ComboBox()

        textAreaContainer = JBScrollPane(
            textArea,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        )

        devicesComboBox = ComboBox()

        progressBar = DelayedProgressBar("Process loading device")

        initialErrorLabel = JBLabel()

        disposable.defineNestedLifetime()
            .bracketIfAlive(opening = {
                component.state
                    .subscribe(
                        { state ->
                            val isSuccessInitializedComponent = state
                                .run { isProcessInitialize || isInitializedError }
                                .not()

                            devicesComboBox.isVisible = isSuccessInitializedComponent
                            textAreaContainer.isVisible = isSuccessInitializedComponent
                            progressBar.delaySetVisibility(state.isProcessInitialize)
                            initialErrorLabel.isVisible = state.isInitializedError
                            initialErrorLabel.text = state.errorMessage
                            myOKAction.isEnabled = state.isAvailableSendBarcode

                            val border = when (isSuccessInitializedComponent) {
                                true -> titleBorder
                                false -> emptyBorder
                            }

                            if (dialogPanel.border !== border) {
                                dialogPanel.border = border
                            }

                            if (state.barcode != textArea.text) {
                                textArea.text = state.barcode.orEmpty()
                            }

                            devicesComboBox.model = DefaultComboBoxModel(state.items.toTypedArray())
                                .apply { selectedItem = state.selectedItem }
                        },
                        logger::error
                    )
                    .let(viewDisposables::add)

                component.events
                    .subscribe(
                        { event ->
                            when (event) {
                                Event.CloseDialog -> close(OK_EXIT_CODE)
                                Event.ShowMultipleChoice -> multipleChoiceDialogFactory.create().show()
                                Event.ShowMultipleGenerateBarcodeDialog -> MultipleGeneratorDialog(this).show()
                                is Event.ShowErrorSendCode -> {
                                    NotificationGroupManager.getInstance()
                                        .getNotificationGroup("Barcode Sender Notification Group")
                                        .createNotification(
                                            "Error sending codes",
                                            event.throwable::class.java.name,
                                            NotificationType.ERROR
                                        )
                                        .notify(project)
                                }

                                is Event.ShowBarcodeTypeList -> {
                                    BarcodePopupMenu(event.list) { event ->
                                        event.actionCommand?.let(
                                            choiceGenerateBarcodeItem::onNext
                                        )
                                    }.show(textArea, event.point.x, event.point.y)
                                }
                            }
                        },
                        logger::error
                    )

                component.bindView(this)
            },
                terminationAction = {
                    viewDisposables.clear()
                    component.unbindView()
                    component.dispose()
                }
            )

        super.init()
    }

    override fun createCenterPanel(): JComponent {
        val renderer = DefaultListRenderer({ item ->
            when (item) {
                is DeviceItem.Device -> item.name
                is DeviceItem.EmptyItem -> item.text
                is DeviceItem.MultipleChoiceItem -> item.text
                else -> null
            }
        }, { item ->
            when (item) {
                is DeviceItem.Device -> BarcodeSenderIcons.Device
                is DeviceItem.MultipleChoiceItem -> BarcodeSenderIcons.MultipleDevice
                is DeviceItem.EmptyItem -> null
                else -> null
            }
        })

        return DialogPanel(layout = BorderLayout())
            .also { dialogPanel ->
                textAreaContainer.apply {
                    isVisible = false
                    preferredSize = Dimension(100, 100)
                }.let { scrollPane ->
                    val centerPanel = JPanel()
                    centerPanel.layout = OverlayLayout(centerPanel)

                    centerPanel.add(progressBar.apply {
                        isVisible = false
                        alignmentX = 0.5f
                        alignmentY = 0.5f
                    })
                    centerPanel.add(initialErrorLabel.apply {
                        isVisible = false
                        alignmentX = 0.5f
                        alignmentY = 0.5f
                    })
                    centerPanel.add(scrollPane.apply { isVisible = false })

                    dialogPanel.add(centerPanel, BorderLayout.CENTER)
                    dialogPanel.add(devicesComboBox.apply {
                        isVisible = false
                        this.renderer = renderer
                    }, BorderLayout.NORTH)

                    scrollPane.minimumSize = Dimension(100, 100)
                }
            }
            .also { dialogPanel = it }
    }

    override fun createDefaultActions() {
        super.createDefaultActions()

        setOKButtonText("Send")
    }

    override fun doOKAction() {
        okActionSubject.onNext(Unit)
    }
}