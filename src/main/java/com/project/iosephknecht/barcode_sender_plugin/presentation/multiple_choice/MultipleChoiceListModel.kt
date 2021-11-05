package com.project.iosephknecht.barcode_sender_plugin.presentation.multiple_choice

import com.project.iosephknecht.barcode_sender_plugin.presentation.multiple_choice.component.MultipleChoiceComponent
import javax.swing.DefaultListModel
import javax.swing.ListModel
import javax.swing.event.ListDataListener

/**
 * Simple proxy for read - only [ListModel].
 *
 * @see [ListModel]
 *
 * @author IosephKnecht
 */
internal class MultipleChoiceListModel(
    devices: Collection<MultipleChoiceComponent.Device> = emptyList()
) : ListModel<MultipleChoiceComponent.Device> {

    private val delegate = DefaultListModel<MultipleChoiceComponent.Device>()

    init {
        delegate.addAll(devices)
    }

    override fun getSize(): Int = delegate.size

    override fun getElementAt(index: Int): MultipleChoiceComponent.Device = delegate.getElementAt(index)

    override fun addListDataListener(l: ListDataListener) = delegate.addListDataListener(l)

    override fun removeListDataListener(l: ListDataListener) = delegate.removeListDataListener(l)
}