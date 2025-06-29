package com.bottomsheettest.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bottomsheettest.app.data.MessageDao
import com.bottomsheettest.app.models.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dao: MessageDao
) : ViewModel() {

    private val _message: MutableStateFlow<String> = MutableStateFlow("")
    private val _messages = dao.observeMessages()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )
    private val _state: MutableStateFlow<MessageState> = MutableStateFlow(MessageState())

    val state: StateFlow<MessageState> =
        combine(_state, _message, _messages) { state, message, messages ->
            state.copy(
                messages = messages,
                currentMessage = message
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MessageState()
        )

    fun onEvent(event: MessageEvent) {
        when (event) {
            is MessageEvent.SetMessage -> {
                viewModelScope.launch {
                    _message.emit(event.text)
                }
            }

            is MessageEvent.SaveMessage -> {
                val message: String = state.value.currentMessage

                if (message.isNotEmpty()) {
                    val payload = Message(
                        text = message,
                        timestamp = System.currentTimeMillis()
                    )

                    viewModelScope.launch {
                        dao.upsertMessage(payload)
                        _message.emit("") // Clear the message after saving.
                    }
                }
            }

            is MessageEvent.DeleteMessage -> {
                viewModelScope.launch {
                    dao.deleteMessage(event.payload)
                }
            }
        }
    }
}