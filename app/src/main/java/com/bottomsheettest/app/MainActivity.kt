package com.bottomsheettest.app

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.imeAnimationTarget
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.bottomsheettest.app.ui.components.AppTopBar
import com.bottomsheettest.app.ui.components.ListContent
import com.bottomsheettest.app.ui.components.RemovalAlert
import com.bottomsheettest.app.ui.components.SheetContent
import com.bottomsheettest.app.ui.theme.BottomSheetTestTheme
import com.bottomsheettest.app.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @SuppressLint("ConfigurationScreenWidthHeight")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel = hiltViewModel<MainViewModel>()
            val scaffoldState = rememberBottomSheetScaffoldState(
                bottomSheetState = rememberStandardBottomSheetState(
                    initialValue = SheetValue.PartiallyExpanded,
                    skipHiddenState = true
                )
            )
            val state = viewModel.state.collectAsStateWithLifecycle()
            val focusRequester = remember { FocusRequester() }
            val scope: CoroutineScope = rememberCoroutineScope()
            val screenHeight: Int = LocalConfiguration.current.screenHeightDp
            val density = LocalDensity.current
            val imeOffset = WindowInsets.imeAnimationTarget.getBottom(density)
            val windowWidthSizeClass: WindowWidthSizeClass = currentWindowAdaptiveInfo()
                .windowSizeClass.windowWidthSizeClass
            val windowHeightSizeClass: WindowHeightSizeClass = currentWindowAdaptiveInfo()
                .windowSizeClass.windowHeightSizeClass
            val sheetPeekHeight by remember (imeOffset, windowWidthSizeClass, windowHeightSizeClass) {
                mutableIntStateOf(
                    when (imeOffset) {
                        0 -> when {
                            windowHeightSizeClass == WindowHeightSizeClass.COMPACT
                                    && windowWidthSizeClass == WindowWidthSizeClass.EXPANDED -> Constants.SHEET_PEEK_HEIGHT_LOW
                            else -> Constants.SHEET_PEEK_HEIGHT_HIGH
                        }
                        else -> when (windowWidthSizeClass) {
                            WindowWidthSizeClass.COMPACT -> imeOffset / 2
                            else -> imeOffset - (imeOffset / 3)
                        }
                    }
                )
            }
            var showDialogState by remember { mutableStateOf(false) }

            SideEffect {
                focusRequester.requestFocus()
            }

            // Collapse the bottom sheet on the system 'back' tap when the input field is not focused.
            BackHandler(
                enabled = scaffoldState.bottomSheetState.hasExpandedState
            ) {
                scope.launch {
                    scaffoldState.bottomSheetState.partialExpand()
                }
            }

            BackHandler(
                enabled = state.value.viewState is ViewState.Select
            ) {
                viewModel.onEvent(MessageEvent.SetViewState(ViewState.ReadAndWrite))
            }

            BottomSheetTestTheme {
                BottomSheetScaffold(
                    scaffoldState = scaffoldState,
                    sheetShape = RoundedCornerShape(
                        topStart = 12.dp,
                        topEnd = 12.dp
                    ),
                    sheetPeekHeight = sheetPeekHeight.dp,
                    topBar = {
                        AppTopBar(
                            viewState = state.value.viewState,
                            onEdit = { id ->
                                viewModel.onEvent(MessageEvent.SetViewState(ViewState.Edit(id)))
                                focusRequester.requestFocus()
                            },
                            onDelete = { showDialogState = true },
                            onResetViewMode = {
                                viewModel.onEvent(MessageEvent.SetViewState(ViewState.ReadAndWrite))
                            }
                        )
                    },
                    sheetContent = {
                        SheetContent(
                            focusRequester = focusRequester,
                            currentMessage = state.value.currentMessage,
                            screenHeight = screenHeight,
                            onValueChange = { newMessage ->
                                viewModel.onEvent(MessageEvent.SetMessage(newMessage))
                            },
                            onSave = { viewModel.onEvent(MessageEvent.SaveMessage) }
                        )
                    },
                    content = { innerPadding ->
                        ListContent(
                            modifier = Modifier
                                .padding(innerPadding),
                            messages = state.value.messages,
                            viewState = state.value.viewState,
                            onClick = { id ->
                                viewModel.onEvent(MessageEvent.SelectMessage(id))
                            },
                            onLongClick = { id ->
                                viewModel.onEvent(
                                    MessageEvent.SetViewState(ViewState.Select(listOf(id)))
                                )
                            }
                        )

                        if (showDialogState) {
                            RemovalAlert(
                                onDelete = {
                                    viewModel.onEvent(MessageEvent.DeleteMessages)
                                    showDialogState = false
                                },
                                onDismiss = { showDialogState = false }
                            )
                        }
                    }
                )
            }
        }
    }
}