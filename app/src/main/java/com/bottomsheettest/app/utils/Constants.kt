package com.bottomsheettest.app.utils

import androidx.compose.material3.TopAppBar

object Constants {
    /**
     * Default height of the app bar in dp.
     * @see TopAppBar
     */
    internal const val APP_BAR_HEIGHT = 64

    /**
     * Bottom sheet height when collapsed (portrait mode).
     * Relevant when keyboard is not being used.
     */
    internal const val SHEET_PEEK_HEIGHT_HIGH = 300

    /**
     * Bottom sheet height when collapsed (landscape mode).
     * Relevant when keyboard is not being used.
     */
    internal const val SHEET_PEEK_HEIGHT_LOW = 120
}