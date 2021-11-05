package com.project.iosephknecht.barcode_sender_plugin.domain

import java.util.*

/**
 * Contract for string provider.
 *
 * @author IosephKnecht
 */
internal interface StringProvider {

    /**
     * Enum for available string constants.
     *
     * @param value string.
     *
     * @author IosephKnecht
     */
    enum class PlainString(val value: String) {
        PLUGIN_NAME("Barcode Sender Plugin");
    }

    /**
     * Enum for available string resources.
     *
     * @param key key of string resource.
     *
     * @author IosephKnecht
     */
    enum class StringResource(val key: String) {
        NOT_HAVE_SELECT_OPTION_TITLE("barcode_sender.device_selector.not_have_select_option_title"),
        MULTIPLE_CHOICE_OPTION_TITLE("barcode_sender.device_selector.multiple_choice_option_title"),
        MULTIPLE_CHOICE_OPTION_TITLE_COUNT("barcode_sender.device_selector.multiple_choice_option_title_count"),
        GENERATE_MULTIPLE_OPTION_TITLE("barcode_sender.recent_barcode_type.generate_multiple_option_title"),
        UNINITIALIZE_BARCODE_RECEIVER_KEY_ERROR_MESSAGE("barcode_sender.settings.uninitialize_barcode_receiver_key_error_message"),
        EMPTY_BARCODE_RECEIVER_KEY_ERROR_MESSAGE("barcode_sender.settings.empty_barcode_receiver_key_error_message"),
        UNKNOWN_ERROR_MESSAGE("barcode_sender.unknown_error_message")
    }

    /** @SelfDocumented **/
    fun getString(resource: StringResource): String?
}

/**
 * Default implementation [StringProvider].
 *
 * @author IosephKnecht
 */
internal class DefaultStringProvider : StringProvider {

    private val resources by lazy {
        ResourceBundle.getBundle("strings")
    }

    override fun getString(resource: StringProvider.StringResource): String? {
        return resources.getString(resource.key)
    }
}