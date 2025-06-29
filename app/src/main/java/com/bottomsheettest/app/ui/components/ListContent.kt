package com.bottomsheettest.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bottomsheettest.app.ViewState
import com.bottomsheettest.app.models.Message

@Composable
internal fun ListContent(
    modifier: Modifier = Modifier,
    messages: List<Message>,
    viewState: ViewState,
    onClick: (Int) -> Unit,
    onLongClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        reverseLayout = true,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(
            horizontal = 20.dp,
            vertical = 12.dp
        ),
        content = {
            items(messages) { message ->
                Message(
                    payload = message,
                    viewState = viewState,
                    onClick = onClick,
                    onLongClick = onLongClick
                )
            }
        }
    )
}

@Composable
private fun Message(
    modifier: Modifier = Modifier,
    payload: Message,
    viewState: ViewState,
    onClick: (Int) -> Unit,
    onLongClick: (Int) -> Unit
) {
    val canSelect: Boolean by remember(viewState) {
        mutableStateOf(viewState is ViewState.Select)
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.extraSmall)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer
                )
                .combinedClickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = if (canSelect) ripple() else null,
                    onClick = {
                        if (canSelect) {
                            onClick(payload.id)
                        }
                    },
                    onLongClick = { onLongClick(payload.id) }
                )
                .padding(12.dp)
        ) {
            Text(
                text = payload.text,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        Text(
            modifier = modifier.fillMaxWidth(),
            text = payload.createdAt.toString(),
            style = MaterialTheme.typography.labelSmall.copy(
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.End
            )
        )
    }
}