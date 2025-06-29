package com.bottomsheettest.app

import com.bottomsheettest.app.models.Message

data class MessageState(
    val messages: List<Message> = emptyList(),
    val currentMessage: String = ""
)