package com.project.iosephknecht.barcode_sender_plugin.presentation.multiple_choice

import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.rd.defineNestedLifetime
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBList
import com.intellij.ui.components.JBScrollPane
import com.project.iosephknecht.barcode_sender_plugin.domain.StringProvider
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.devices.DevicesFeatureContract
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.devices_selector.DevicesSelectorFeatureContract
import com.project.iosephknecht.barcode_sender_plugin.presentation.multiple_choice.component.MultipleChoiceComponent
import com.project.iosephknecht.barcode_sender_plugin.presentation.multiple_choice.component.MultipleChoiceListener
import com.project.iosephknecht.barcode_sender_plugin.presentation.multiple_choice.di.DaggerMultipleChoiceComponent
import hu.akarnokd.rxjava3.swing.SwingObservable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.SerialDisposable
import io.reactivex.rxjava3.subjects.PublishSubject
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.event.MouseEvent
import javax.inject.Inject
import javax.swing.JComponent
import javax.swing.JList
import javax.swing.JPanel
import javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
import javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED

/**
 * Dialog for [MultipleChoiceComponent].
 *
 * @author IosephKnecht
 */
internal class MultipleChoiceDevicesDialog(
    devicesFeature: DevicesFeatureContract.Feature,
    devicesSelector: DevicesSelectorFeatureContract.Feature,
    private val listener: MultipleChoiceListener
) : DialogWrapper(null, false),
    MultipleChoiceComponent.View {

    @set:Inject
    internal lateinit var component: MultipleChoiceComponent

    private val logger by lazy { logger<MultipleChoiceDevicesDialog>() }
    private val componentDisposable = SerialDisposable()
    private lateinit var devicesList: JList<MultipleChoiceComponent.Device>

    private val leftMouseClick by lazy {
        SwingObservable.mouse(devicesList)
            .filter { event ->
                with(event) {
                    (id == MouseEvent.MOUSE_CLICKED)
                        .and(button == MouseEvent.BUTTON1)
                        .and(clickCount == 1)
                }
            }
            .map { event ->
                @Suppress("UNCHECKED_CAST")
                val source = event.source as JList<MultipleChoiceComponent.Device>
                val index = source.locationToIndex(event.point)

                source.model.getElementAt(index)
            }
            .share()
    }

    override val selectDeviceObservable: Observable<MultipleChoiceComponent.Device>
        get() = leftMouseClick.filter { !it.isSelected }

    override val resetSelectDeviceObservable: Observable<MultipleChoiceComponent.Device>
        get() = leftMouseClick.filter { it.isSelected }

    override val successChoiceObservable: PublishSubject<Unit> = PublishSubject.create()

    override val interruptChoiceObservable: PublishSubject<Unit> = PublishSubject.create()

    init {
        devicesList = JBList<MultipleChoiceComponent.Device>().apply {
            setFixedCellHeight(40)
        }

        DaggerMultipleChoiceComponent
            .factory()
            .create(
                devicesSelector = devicesSelector,
                devicesFeature = devicesFeature
            )
            .inject(this)

        init()
    }

    override fun init() {
        this.title = StringProvider.PlainString.PLUGIN_NAME.value

        disposable.defineNestedLifetime()
            .bracketIfAlive(opening = {
                component.states
                    .subscribe(
                        { state ->
                            okAction.isEnabled = state.selectedDeviceCount > 1
                            devicesList.model = MultipleChoiceListModel(state.deviceList)
                        },
                        logger::error
                    )
                    .let(componentDisposable::set)

                component.events
                    .subscribe(
                        { event ->
                            when (event) {
                                MultipleChoiceComponent.Event.InterruptChoice -> {
                                    listener.interruptChoice()
                                    super.doCancelAction()
                                }

                                is MultipleChoiceComponent.Event.SuccessChoice -> {
                                    listener.successChoice(event.devices)
                                    super.doOKAction()
                                }
                            }.let { }
                        },
                        logger::error
                    )

                component.bindView(this)
            },
                terminationAction = {
                    component.unbindView()
                    componentDisposable.dispose()
                }
            )

        super.init()
    }

    override fun doOKAction() {
        successChoiceObservable.onNext(Unit)
    }

    override fun doCancelAction() {
        interruptChoiceObservable.onNext(Unit)
    }

    override fun createCenterPanel(): JComponent {
        return JPanel(BorderLayout()).apply {
            preferredSize = Dimension(100, 100)

            JBScrollPane(
                devicesList.apply { cellRenderer = MultipleChoiceCellRenderer() },
                VERTICAL_SCROLLBAR_AS_NEEDED,
                HORIZONTAL_SCROLLBAR_NEVER
            ).let { scrollPane ->
                scrollPane.setViewportView(devicesList)
                add(scrollPane, BorderLayout.CENTER)
            }
        }
    }
}