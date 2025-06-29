package com.bottomsheettest.app

sealed class ViewState {
    data object ReadAndWrite : ViewState()
    data class Select(val ids: List<Int> = emptyList()) : ViewState()
    data class Edit(val id: Int) : ViewState()
}