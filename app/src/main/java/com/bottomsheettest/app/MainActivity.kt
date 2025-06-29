package com.bottomsheettest.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bottomsheettest.app.ui.components.AppTopBar
import com.bottomsheettest.app.ui.components.ListContent
import com.bottomsheettest.app.ui.components.SheetContent
import com.bottomsheettest.app.ui.theme.BottomSheetTestTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
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

            SideEffect {
                focusRequester.requestFocus()
            }

            BottomSheetTestTheme {
                BottomSheetScaffold(
                    scaffoldState = scaffoldState,
                    sheetShape = RoundedCornerShape(
                        topStart = 12.dp,
                        topEnd = 12.dp
                    ),
                    topBar = {
                        AppTopBar(
                            viewState = state.value.viewState,
                            onEdit = { id ->
                                viewModel.onEvent(MessageEvent.SetViewState(ViewState.Edit(id)))
                            },
                            onDelete = {
                                viewModel.onEvent(MessageEvent.DeleteMessages)
                            },
                            onResetViewMode = {
                                viewModel.onEvent(MessageEvent.SetViewState(ViewState.ReadAndWrite))
                            }
                        )
                    },
                    sheetContent = {
                        SheetContent(
                            focusRequester = focusRequester,
                            currentMessage = state.value.currentMessage,
                            onValueChange = { newMessage ->
                                viewModel.onEvent(MessageEvent.SetMessage(newMessage))
                            },
                            onSave = {
                                viewModel.onEvent(MessageEvent.SaveMessage)
                            }
                        )
                    },
                    sheetPeekHeight = 180.dp,
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
                    }
                )
            }
        }
    }
}