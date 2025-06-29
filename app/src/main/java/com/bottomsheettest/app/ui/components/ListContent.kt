package com.bottomsheettest.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bottomsheettest.app.models.Message

@Composable
internal fun ListContent(
    modifier: Modifier = Modifier,
    messages: List<Message>
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(
                WindowInsets.safeContent
            ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(
            top = 12.dp,
            bottom = 300.dp
        ),
        content = {
            items(messages) { message ->
                Message(
                    payload = message,
                    onLongClick = {}
                )
            }
        }
    )
}

@Composable
private fun Message(
    modifier: Modifier = Modifier,
    payload: Message,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.extraSmall)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer
                )
                .padding(12.dp)
                .combinedClickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(),
                    onClick = onClick,
                    onLongClick = onLongClick
                ),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = payload.text,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        Text(
            modifier = modifier.fillMaxWidth(),
            text = payload.timestamp.toString(),
            style = MaterialTheme.typography.labelSmall.copy(
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.End
            )
        )
    }
}