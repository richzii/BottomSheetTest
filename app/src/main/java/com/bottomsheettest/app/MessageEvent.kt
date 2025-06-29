package com.bottomsheettest.app

import com.bottomsheettest.app.models.Message

sealed class MessageEvent {
    data object SaveMessage : MessageEvent()
    data class SetMessage(val text: String) : MessageEvent()
    data class DeleteMessage(val payload: Message) : MessageEvent()
}