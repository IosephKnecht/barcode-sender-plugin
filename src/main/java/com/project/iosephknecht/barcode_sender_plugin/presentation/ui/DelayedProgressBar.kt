package com.project.iosephknecht.barcode_sender_plugin.presentation.ui

import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.panels.VerticalLayout
import java.awt.LayoutManager2
import java.awt.event.ActionListener
import javax.swing.JPanel
import javax.swing.JProgressBar
import javax.swing.Timer

/**
 * Extension - function for delay set visibility.
 *
 * @param isVisible
 *
 * @see DelayedProgressBar.delayedShow
 * @see DelayedProgressBar.delayedHide
 */
internal fun DelayedProgressBar.delaySetVisibility(isVisible: Boolean) = when (isVisible) {
    true -> delayedShow()
    false -> delayedHide()
}

/**
 * Component for progress bar and above label.
 *
 * Dummy vellum [ContentLoadingProgressBar](https://developer.android.com/reference/androidx/core/widget/ContentLoadingProgressBar)
 *
 * @author IosephKnecht
 */
@Suppress("unused")
internal class DelayedProgressBar : JPanel {

    private val title: String?
    private val showTimer: Timer
    private val hideTimer: Timer

    constructor() : this(null)

    constructor(title: String?) : this(true, title)

    constructor(isDoubleBuffered: Boolean, title: String?) : this(VerticalLayout(10), isDoubleBuffered, title)

    private constructor(
        layoutManager2: LayoutManager2,
        isDoubleBuffered: Boolean,
        title: String?
    ) : super(
        layoutManager2,
        isDoubleBuffered
    ) {
        this.title = title

        title?.run(::JBLabel)?.let(::add)
        add(JProgressBar().apply { isIndeterminate = true })

        showTimer = Timer(500, showAction).apply { isRepeats = false }
        hideTimer = Timer(500, hideAction).apply { isRepeats = false }
    }

    private val showAction = ActionListener {
        isVisible = true
    }

    private val hideAction = ActionListener {
        isVisible = false
    }


    /**
     * Show the progress view after waiting for a minimum delay.
     */
    fun delayedShow() {
        hideTimer.stop()
        showTimer.start()
    }

    /**
     * Hide the progress view if it is visible.
     */
    fun delayedHide() {
        showTimer.stop()
        hideTimer.start()
    }
}