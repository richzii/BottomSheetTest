package com.bottomsheettest.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bottomsheettest.app.R
import com.bottomsheettest.app.ViewState

@Composable
internal fun AppTopBar(
    modifier: Modifier = Modifier,
    viewState: ViewState,
    onEdit: (Int) -> Unit,
    onDelete: () -> Unit,
    onResetViewMode: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = when (viewState) {
                    ViewState.ReadAndWrite -> stringResource(R.string.top_bar_title)
                    is ViewState.Edit -> stringResource(R.string.top_bar_edit_title)
                    is ViewState.Select -> stringResource(
                        R.string.top_bar_selected_title,
                        viewState.ids.size
                    )
                }
            )
        },
        navigationIcon = {
            if (viewState != ViewState.ReadAndWrite) {
                Box(
                    modifier = modifier
                        .padding(horizontal = 12.dp)
                ) {
                    Icon(
                        modifier = modifier
                            .clickable(
                                enabled = viewState != ViewState.ReadAndWrite,
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(
                                    bounded = false,
                                    radius = 20.dp
                                ),
                                onClick = onResetViewMode
                            ),
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear view state"
                    )
                }
            }
        },
        actions = {
            when (viewState) {
                is ViewState.Select -> {
                    Row(
                        modifier = modifier
                            .padding(end = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        AnimatedVisibility(
                            visible = viewState.ids.size == 1
                        ) {
                            Icon(
                                modifier = modifier.clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = ripple(
                                        bounded = false,
                                        radius = 20.dp
                                    ),
                                    onClick = { viewState.ids.firstOrNull()?.let(onEdit) }
                                ),
                                imageVector = Icons.Filled.Edit,
                                contentDescription = "Edit"
                            )
                        }

                        Icon(
                            modifier = modifier.clickable(
                                enabled = viewState.ids.isNotEmpty(),
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(
                                    bounded = false,
                                    radius = 20.dp
                                ),
                                onClick = onDelete
                            ),
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete"
                        )
                    }
                }

                else -> Unit // No actions for other states
            }
        },
        colors = TopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            scrolledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            navigationIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            actionIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}