package com.bottomsheettest.app.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.bottomsheettest.app.R

@Composable
internal fun SheetContent(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        MessageInputField()
    }
}

@Preview(showBackground = true)
@Composable
private fun MessageInputField(
    modifier: Modifier = Modifier,
    valueState: MutableState<String> = mutableStateOf("")
) {
    TextField(
        modifier = modifier.fillMaxWidth(),
        value = valueState.value,
        placeholder = {
            Text(
                text = stringResource(R.string.message_placeholder),
                style = MaterialTheme.typography.bodySmall
            )
        },
        onValueChange = { newValue ->
            valueState.value = newValue
        }
    )
}