package com.project.iosephknecht.barcode_sender_plugin.presentation.features.common

/**
 * Dummy - contract of logger.
 *
 * While it is used to hide the applied logging logic.
 *
 * @author IosephKnecht
 */
internal fun interface Logger {

    /** @SelfDocumented **/
    fun error(throwable: Throwable)

    /**
     * Extension point for Logger contract.
     */
    companion object {

        /**
         * Default print stack trace logger.
         */
        fun printStackTraceLogger() = Logger { it.printStackTrace() }
    }
}