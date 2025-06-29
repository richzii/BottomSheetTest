package com.bottomsheettest.app

import com.bottomsheettest.app.models.Message

data class MessagesState(
    val messages: List<Message> = emptyList(),
    val currentMessage: String = "",
    val viewState: ViewState = ViewState.ReadAndWrite
)