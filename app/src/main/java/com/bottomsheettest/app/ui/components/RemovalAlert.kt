package com.bottomsheettest.app.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.bottomsheettest.app.R

@Composable
internal fun RemovalAlert(
    modifier: Modifier = Modifier,
    onDelete: () -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    BasicAlertDialog(
        content = {
            Surface(
                modifier = modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
                    .clip(MaterialTheme.shapes.large),
                tonalElevation = AlertDialogDefaults.TonalElevation
            ) {
                Column(
                    modifier = modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.removal_alert_title),
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = stringResource(R.string.removal_alert_subtitle),
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Row(
                        modifier = modifier
                            .align(Alignment.End)
                            .padding(top = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ActionButton(
                            modifier = modifier.semantics { contentDescription = "Cancel" },
                            titleRes = R.string.removal_alert_negative_action,
                            onClick = onDismiss
                        )

                        ActionButton(
                            modifier = modifier.semantics { contentDescription = "Delete" },
                            titleRes = R.string.removal_alert_positive_action,
                            onClick = onDelete
                        )
                    }
                }
            }
        },
        onDismissRequest = onDismiss
    )
}

@Composable
private fun ActionButton(
    modifier: Modifier,
    @StringRes titleRes: Int,
    onClick: () -> Unit
) {
    TextButton(
        content = {
            Text(
                text = stringResource(titleRes),
                style = MaterialTheme.typography.labelLarge.copy(
                    color = MaterialTheme.colorScheme.secondary
                )
            )
        },
        onClick = onClick
    )
}