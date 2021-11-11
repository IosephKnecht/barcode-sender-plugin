package com.project.iosephknecht.barcode_sender_plugin.presentation.sender.component

import com.android.ddmlib.IDevice
import com.intellij.openapi.diagnostic.Logger
import com.project.iosephknecht.barcode_sender_plugin.data.BarcodeType
import com.project.iosephknecht.barcode_sender_plugin.domain.StringProvider
import com.project.iosephknecht.barcode_sender_plugin.domain.StringProvider.StringResource
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_generator.BarcodeGeneratorFeatureContract
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.news
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.states
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.device_selector.DeviceSelectorFeatureContract
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.device_selector.multipleChoiceDevices
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.devices.DevicesFeatureContract
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.devices_selector.DevicesSelectorFeatureContract
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.barcode_editor.BarcodeEditorFeatureContract
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.SchedulersContainer
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.common.Swing
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.recent_barcode_type.RecentBarcodeTypeFeatureContract
import com.project.iosephknecht.barcode_sender_plugin.presentation.utils.deviceName
import hu.akarnokd.rxjava3.swing.SwingSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import com.project.iosephknecht.barcode_sender_plugin.presentation.features.device_selector.DeviceSelectorFeatureContract.News as DeviceSelectorNews

/**
 * Implementation [BarcodeComponent].
 *
 * @param devicesFeature
 * @param deviceSelectorFeature
 * @param barcodeEditorFeature
 *
 * @author IosephKnecht.
 */
internal class BarcodeComponentDefault(
    devicesFeature: DevicesFeatureContract.Feature,
    devicesSelectorFeature: DevicesSelectorFeatureContract.Feature,
    private val stringProvider: StringProvider,
    private val deviceSelectorFeature: DeviceSelectorFeatureContract.Feature,
    private val barcodeEditorFeature: BarcodeEditorFeatureContract.Feature,
    private val barcodeGeneratorFeature: BarcodeGeneratorFeatureContract.Feature,
    private val recentBarcodeTypeFeature: RecentBarcodeTypeFeatureContract.Feature,
    private val logger: Logger,
    schedulersContainer: SchedulersContainer = SchedulersContainer.Swing
) : BarcodeComponent {

    override val state: BehaviorSubject<BarcodeComponent.State> = BehaviorSubject.create()
    override val events: PublishSubject<BarcodeComponent.Event> = PublishSubject.create()

    private val featureDisposables = CompositeDisposable()
    private val viewDisposables = CompositeDisposable()

    init {
        featureDisposables.add(devicesFeature)
        featureDisposables.add(devicesSelectorFeature)
        featureDisposables.add(deviceSelectorFeature)
        featureDisposables.add(barcodeEditorFeature)
        featureDisposables.add(barcodeGeneratorFeature)
        featureDisposables.add(recentBarcodeTypeFeature)

        deviceSelectorFeature.news
            .filter { news ->
                news::class !in arrayOf(
                    DeviceSelectorNews.ResetSelected::class,
                    DeviceSelectorNews.SingleChoiceDevice::class
                )
            }
            .map { news ->
                when (news) {
                    DeviceSelectorNews.SuccessfulSendCode -> BarcodeComponent.Event.CloseDialog
                    DeviceSelectorNews.MultipleChoiceDevice -> BarcodeComponent.Event.ShowMultipleChoice
                    is DeviceSelectorNews.FailureSendCode -> BarcodeComponent.Event.ShowErrorSendCode(news.throwable)
                    else -> null
                }!!
            }
            .subscribe(
                events::onNext,
                logger::error
            )
            .let(featureDisposables::add)

        Observable.combineLatest(
            devicesFeature.states.distinctUntilChanged(),
            deviceSelectorFeature.states.distinctUntilChanged(),
            devicesSelectorFeature.states.distinctUntilChanged(),
            barcodeEditorFeature.states.distinctUntilChanged()
        ) { devicesState, selectorDeviceState, selectorDevicesState, barcodeEditorState ->
            val isInitialized = devicesState.isInitialized
                .and(selectorDeviceState.isInitialized)
                .and(barcodeEditorState.isInitialized)

            val isInitializedError = devicesState.isInitializedError
                .or(selectorDeviceState.isInitializedError)

            val isProcessInitialize = devicesState.isProcessInitialize
                .or(selectorDeviceState.isProcessInitialize)

            var selectedItem: BarcodeComponent.DeviceItem? = null
            var items: List<BarcodeComponent.DeviceItem>? = null
            var barcode: String? = null
            var isAvailableSendBarcode = false
            var errorMessageResource: StringResource? = null

            if (isInitialized) {
                val devices = devicesState.items?.map { it.toDevice() }.orEmpty()

                val emptyItem = StringResource.NOT_HAVE_SELECT_OPTION_TITLE
                    .let(stringProvider::getString)
                    .let(::requireNotNull)
                    .run(BarcodeComponent.DeviceItem::EmptyItem)

                val defaultMultipleChoiceItem = StringResource.MULTIPLE_CHOICE_OPTION_TITLE
                    .takeIf { devices.size > 1 }
                    ?.let(stringProvider::getString)
                    ?.run(BarcodeComponent.DeviceItem::MultipleChoiceItem)

                val selectedDevice = selectorDeviceState.selectedItem?.toDevice()

                val selectedMultipleChoiceItem = when (devices.size > 1) {
                    true -> selectorDeviceState.multipleChoiceDevices?.let {
                        when (val size = it.size) {
                            0 -> defaultMultipleChoiceItem
                            else -> stringProvider.getString(StringResource.MULTIPLE_CHOICE_OPTION_TITLE_COUNT)
                                .let(::requireNotNull)
                                .let { title -> "$title ($size)" }
                                .run(BarcodeComponent.DeviceItem::MultipleChoiceItem)
                        }
                    }
                    false -> null
                }

                selectedItem = selectedDevice ?: selectedMultipleChoiceItem ?: emptyItem
                items = listOfNotNull(emptyItem, selectedMultipleChoiceItem ?: defaultMultipleChoiceItem) + devices
                barcode = barcodeEditorState.barcode
                isAvailableSendBarcode = barcode.isNullOrBlank().not() && selectorDeviceState.isAvailableSendBarcode
            }

            if (isInitializedError) {
                errorMessageResource = selectorDeviceState.errorReason?.let {
                    when (it) {
                        DeviceSelectorFeatureContract.ErrorReason.UNINITIALIZED_BARCODE_RECEIVER_KEY -> StringResource.UNINITIALIZE_BARCODE_RECEIVER_KEY_ERROR_MESSAGE
                        DeviceSelectorFeatureContract.ErrorReason.EMPTY_BARCODE_RECEIVER_KEY -> StringResource.EMPTY_BARCODE_RECEIVER_KEY_ERROR_MESSAGE
                        DeviceSelectorFeatureContract.ErrorReason.UNKNOWN -> StringResource.UNKNOWN_ERROR_MESSAGE
                    }
                }
            }

            if (!isInitialized && errorMessageResource == null) {
                errorMessageResource = StringResource.UNKNOWN_ERROR_MESSAGE
            }

            BarcodeComponent.State(
                items = items.orEmpty(),
                selectedItem = selectedItem,
                barcode = barcode,
                isAvailableSendBarcode = isAvailableSendBarcode,
                isProcessInitialize = isProcessInitialize,
                isInitializedError = isInitializedError,
                errorMessage = errorMessageResource?.let(stringProvider::getString)
            )
        }
            .subscribeOn(schedulersContainer.computation.get())
            .observeOn(SwingSchedulers.edt())
            .doOnSubscribe {
                deviceSelectorFeature.accept(DeviceSelectorFeatureContract.Intent.Initialize)
                barcodeEditorFeature.accept(BarcodeEditorFeatureContract.Intent.Initialize)
                recentBarcodeTypeFeature.accept(RecentBarcodeTypeFeatureContract.Intent.Initialize)
            }
            .subscribe(
                state::onNext,
                logger::error
            )
            .let(featureDisposables::add)
    }

    override fun bindView(view: BarcodeComponent.View) {
        view.changeSelectedItem
            .debounce(300, TimeUnit.MILLISECONDS)
            .map { it.device.let(DeviceSelectorFeatureContract.Intent::SelectDevice) }
            .observeOn(SwingSchedulers.edt())
            .subscribe(
                deviceSelectorFeature::accept,
                logger::error
            )
            .let(viewDisposables::add)

        view.resetSelected
            .debounce(300, TimeUnit.MILLISECONDS)
            .map { DeviceSelectorFeatureContract.Intent.ResetSelected }
            .observeOn(SwingSchedulers.edt())
            .subscribe(
                deviceSelectorFeature::accept,
                logger::error
            )
            .let(viewDisposables::add)

        view.changeBarcode
            .debounce(300, TimeUnit.MILLISECONDS)
            .map { BarcodeEditorFeatureContract.Intent.EditText(it) }
            .observeOn(SwingSchedulers.edt())
            .subscribe(
                barcodeEditorFeature::accept,
                logger::error
            )
            .let(viewDisposables::add)

        view.sendBarcode
            .debounce(500, TimeUnit.MILLISECONDS)
            .map { DeviceSelectorFeatureContract.Intent.SendBarcode(barcodeEditorFeature.state.barcode.orEmpty()) }
            .observeOn(SwingSchedulers.edt())
            .subscribe(
                deviceSelectorFeature::accept,
                logger::error
            )
            .let(viewDisposables::add)

        view.multipleChoiceItem
            .debounce(300, TimeUnit.MILLISECONDS)
            .map { DeviceSelectorFeatureContract.Intent.ChoiceMultipleDevices }
            .observeOn(SwingSchedulers.edt())
            .subscribe(
                deviceSelectorFeature::accept,
                logger::error
            )
            .let(viewDisposables::add)

        view.successMultipleChoice
            .debounce(300, TimeUnit.MILLISECONDS)
            .map(DeviceSelectorFeatureContract.Intent::SuccessMultipleChoice)
            .observeOn(SwingSchedulers.edt())
            .subscribe(
                deviceSelectorFeature::accept,
                logger::error
            )
            .let(viewDisposables::add)

        view.interruptMultipleChoice
            .debounce(300, TimeUnit.MILLISECONDS)
            .map { DeviceSelectorFeatureContract.Intent.CancelMultipleChoice }
            .observeOn(SwingSchedulers.edt())
            .subscribe(
                deviceSelectorFeature::accept,
                logger::error
            )
            .let(viewDisposables::add)

        view.showBarcodeGenerateMenu
            .debounce(300, TimeUnit.MILLISECONDS)
            .map { point ->
                when (val state = recentBarcodeTypeFeature.state) {
                    RecentBarcodeTypeFeatureContract.State.NotInitialized,
                    RecentBarcodeTypeFeatureContract.State.ProcessInitialized -> null
                    is RecentBarcodeTypeFeatureContract.State.Data -> state.recent
                    is RecentBarcodeTypeFeatureContract.State.ProcessChangeRecent -> state.recent
                }?.keys to point
            }
            .filter { (list, _) -> !list.isNullOrEmpty() }
            .map { (list, point) ->
                BarcodeComponent.Event.ShowBarcodeTypeList(
                    list = list!!
                        .map { BarcodeComponent.BarcodeGenerateItem.BarcodeType(it.title, it.name) }
                        .plus(
                            StringResource.GENERATE_MULTIPLE_OPTION_TITLE
                                .let(stringProvider::getString)
                                .let(::requireNotNull)
                                .let { title ->
                                    BarcodeComponent.BarcodeGenerateItem.MultipleGenerateBarcodeItem(
                                        title = title,
                                        actionCode = MULTIPLE_GENERATE_BARCODE_LIST_ACTION_CODE
                                    )
                                }
                        ),
                    point = point
                )
            }
            .observeOn(SwingSchedulers.edt())
            .subscribe(
                events::onNext,
                logger::error
            )

        view.choiceGenerateBarcodeItem
            .debounce(300, TimeUnit.MILLISECONDS)
            .filter { actionCode -> BarcodeType.values().firstOrNull { it.name == actionCode } != null }
            .map { name -> BarcodeType.valueOf(name).let(BarcodeGeneratorFeatureContract.Intent::GenerateBarcode) }
            .observeOn(SwingSchedulers.edt())
            .subscribe(
                barcodeGeneratorFeature::accept,
                logger::error
            )
            .let(viewDisposables::add)

        view.choiceGenerateBarcodeItem
            .debounce(300, TimeUnit.MILLISECONDS)
            .filter { actionCode -> actionCode == MULTIPLE_GENERATE_BARCODE_LIST_ACTION_CODE }
            .map { BarcodeComponent.Event.ShowMultipleGenerateBarcodeDialog }
            .observeOn(SwingSchedulers.edt())
            .subscribe(
                events::onNext,
                logger::error
            )
            .let(viewDisposables::add)

        view.insertMultipleBarcodeList
            .debounce(300, TimeUnit.MILLISECONDS)
            .map { (_, list) ->
                list.joinToString(separator = System.lineSeparator())
                    .run(BarcodeEditorFeatureContract.Intent::AddText)
            }
            .observeOn(SwingSchedulers.edt())
            .subscribe(
                barcodeEditorFeature::accept,
                logger::error
            )
            .let(viewDisposables::add)
    }

    override fun unbindView() {
        viewDisposables.clear()
    }

    override fun dispose() {
        viewDisposables.dispose()
        featureDisposables.clear()
        featureDisposables.dispose()
    }

    private companion object {
        const val MULTIPLE_GENERATE_BARCODE_LIST_ACTION_CODE = "MULTIPLE_GENERATE_BARCODE_LIST_ACTION_CODE"

        fun IDevice.toDevice() = BarcodeComponent.DeviceItem.Device(
            name = deviceName,
            device = this
        )
    }
}