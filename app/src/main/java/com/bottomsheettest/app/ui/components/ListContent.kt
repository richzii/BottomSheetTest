package com.bottomsheettest.app.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.bottomsheettest.app.ViewState
import com.bottomsheettest.app.models.Message
import com.bottomsheettest.app.utils.asReadableDate

@Composable
internal fun ListContent(
    modifier: Modifier = Modifier,
    messages: List<Message>,
    viewState: ViewState,
    onClick: (Int) -> Unit,
    onLongClick: (Int) -> Unit
) {
    val pad = WindowInsets.safeDrawing.asPaddingValues()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(
                start = pad.calculateStartPadding(LayoutDirection.Ltr),
                end = pad.calculateEndPadding(LayoutDirection.Rtl)
            ),
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

    val isSelected: Boolean by remember(viewState) {
        mutableStateOf(
            when (viewState) {
                is ViewState.Edit -> viewState.id == payload.id
                is ViewState.Select -> viewState.ids.contains(payload.id)
                else -> false
            }
        )
    }

    val backgroundColor by animateColorAsState(
        when {
            isSelected -> MaterialTheme.colorScheme.primaryContainer
            else -> MaterialTheme.colorScheme.secondaryContainer
        }
    )

    val textColor by animateColorAsState(
        when {
            isSelected -> MaterialTheme.colorScheme.onPrimaryContainer
            else -> MaterialTheme.colorScheme.onSecondaryContainer
        }
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = modifier
                .semantics { contentDescription = "Message" }
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.extraSmall)
                .background(
                    color = backgroundColor
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
                color = textColor
            )
        }

        Row(
            modifier = modifier
                .semantics { contentDescription = "Time" }
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.End),
            verticalAlignment = Alignment.CenterVertically
        ) {
            payload.updatedAt?.let {
                Icon(
                    modifier = modifier.size(12.dp),
                    imageVector = Icons.Default.Edit,
                    tint = MaterialTheme.colorScheme.secondary,
                    contentDescription = "Edited"
                )
            }

            Text(
                text = payload.updatedAt?.asReadableDate() ?: payload.createdAt.asReadableDate(),
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.secondary
                )
            )
        }
    }
}