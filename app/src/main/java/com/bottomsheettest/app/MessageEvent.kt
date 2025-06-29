package com.bottomsheettest.app

sealed class MessageEvent {
    data object DeleteMessages : MessageEvent()
    data object SaveMessage : MessageEvent()
    data class SetMessage(val text: String) : MessageEvent()
    data class SetViewState(val viewState: ViewState) : MessageEvent()
    data class SelectMessage(val id: Int) : MessageEvent()
}