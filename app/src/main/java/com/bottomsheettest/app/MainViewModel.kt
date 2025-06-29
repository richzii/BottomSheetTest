package com.bottomsheettest.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bottomsheettest.app.data.MessageDao
import com.bottomsheettest.app.models.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
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
    private val _viewState:MutableStateFlow<ViewState> = MutableStateFlow(ViewState.ReadAndWrite)
    private val _state: MutableStateFlow<MessagesState> = MutableStateFlow(MessagesState())

    val state: StateFlow<MessagesState> =
        combine(_state, _message, _messages, _viewState) { state, message, messages, viewState ->
            state.copy(
                messages = messages,
                currentMessage = message,
                viewState = viewState
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MessagesState()
        )

    fun onEvent(event: MessageEvent) {
        when (event) {
            MessageEvent.DeleteMessages -> {
                viewModelScope.launch {
                    when (val viewState = _viewState.value) {
                        ViewState.ReadAndWrite,
                        is ViewState.Edit -> Unit // Should display and log error or warning. Out of scope for this example.
                        is ViewState.Select -> deleteMessages(viewState.ids)
                    }

                    _viewState.emit(ViewState.ReadAndWrite)
                }
            }

            MessageEvent.SaveMessage -> {
                val message: String = state.value.currentMessage

                if (message.isNotEmpty()) {
                    val payload = Message(
                        text = message,
                        createdAt = System.currentTimeMillis()
                    )

                    viewModelScope.launch {
                        dao.upsertMessage(payload)
                        _message.emit("") // Clear the message after saving.
                    }
                }
            }

            is MessageEvent.SetMessage -> {
                viewModelScope.launch {
                    _message.emit(event.text)
                }
            }

            is MessageEvent.SelectMessage -> {
                viewModelScope.launch {
                    when (val viewState = _viewState.value) {
                        ViewState.ReadAndWrite,
                        is ViewState.Edit -> Unit // Should display and log error or warning. Out of scope for this example.
                        is ViewState.Select -> {
                            val newList = viewState.ids.toMutableList()
                            if (event.id in newList) {
                                newList.remove(event.id)
                            } else {
                                newList.add(event.id)
                            }

                            _viewState.emit(ViewState.Select(newList))
                        }
                    }
                }
            }

            is MessageEvent.SetViewState -> {
                viewModelScope.launch {
                    val messageToEdit: Message? = when (val viewState = event.viewState) {
                        is ViewState.Edit -> _messages.value.firstOrNull { it.id == viewState.id }
                        else -> null
                    }

                    listOf(
                        async { _viewState.emit(event.viewState) },
                        async { _message.emit(messageToEdit?.text ?: "") }
                    ).awaitAll()
                }
            }
        }
    }

    private suspend fun deleteMessages(ids: List<Int>) {
        val messagesToDelete = _messages.value.filter { it.id in ids }

        messagesToDelete.forEach { message ->
            dao.deleteMessage(message)
        }
    }
}