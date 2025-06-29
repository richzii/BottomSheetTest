package com.bottomsheettest.app.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bottomsheettest.app.R
import com.bottomsheettest.app.utils.Constants

@Composable
internal fun SheetContent(
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester,
    currentMessage: String,
    screenHeight: Int,
    onValueChange: (String) -> Unit,
    onSave: () -> Unit
) {
    val topPadding: Dp = WindowInsets.systemBars.asPaddingValues().calculateTopPadding()
    val height: Dp by remember(screenHeight, topPadding) {
        mutableStateOf(screenHeight.dp - (topPadding * 3)- Constants.APP_BAR_HEIGHT.dp)
    }

    Row(
        modifier = modifier
            .height(height)
            .padding(
                horizontal = 12.dp
            ),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        MessageInputField(
            modifier = modifier.weight(1f),
            focusRequester = focusRequester,
            currentMessage = currentMessage,
            onValueChange = onValueChange
        )

        SaveButton(
            currentMessage = currentMessage,
            onClick = onSave
        )
    }
}

@Composable
private fun MessageInputField(
    modifier: Modifier,
    focusRequester: FocusRequester,
    currentMessage: String,
    onValueChange: (String) -> Unit
) {
    TextField(
        modifier = modifier
            .semantics { contentDescription = "Message input" }
            .focusRequester(focusRequester)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .fillMaxWidth()
            .heightIn(
                max = 200.dp
            ),
        value = currentMessage,
        placeholder = {
            Text(
                text = stringResource(R.string.message_placeholder),
                style = MaterialTheme.typography.bodySmall
            )
        },
        maxLines = 10,
        onValueChange = onValueChange
    )
}

@Composable
private fun SaveButton(
    modifier: Modifier = Modifier,
    currentMessage: String,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        if (currentMessage.isEmpty()) {
            MaterialTheme.colorScheme.secondaryContainer.copy(
                alpha = 0.5f
            )
        } else {
            MaterialTheme.colorScheme.primaryContainer
        }
    )

    val iconColor by animateColorAsState(
        if (currentMessage.isEmpty()) {
            MaterialTheme.colorScheme.onSecondaryContainer
        } else {
            MaterialTheme.colorScheme.onSecondaryContainer
        }
    )

    Box(
        modifier = modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable(
                enabled = currentMessage.isNotEmpty(),
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(),
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.Send,
            tint = iconColor,
            contentDescription = "Save message"
        )
    }
}