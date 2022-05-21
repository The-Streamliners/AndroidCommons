package com.streamliners.widgetssample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.streamliners.widgetssample.multiselect.MultiSelectWidget
import com.streamliners.widgetssample.ui.theme.CommonsTheme
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CommonsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Content()
                }
            }
        }
    }

    @Composable
    private fun Content() {
        val items = remember {
            listOf("Apple", "Ball", "Cat", "Dog")
        }
        val state = remember {
            mutableStateOf(
                MultiSelectWidget.MultiSelectWidgetState(
                    items = items,
                    selectedItems = emptyList(),
                    type = MultiSelectWidget.MultiSelectType.DisabledItemsSupport.create(
                        items = items,
                        disabledItems = emptyList()
                    )
                )
            )
        }
        val temporaryState = remember {
            mutableStateOf(state.value)
        }

        val bottomSheetState = rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            confirmStateChange = {
                if(it == ModalBottomSheetValue.Hidden) {
                    temporaryState.value = state.value
                }
                true
            }
        )
        val scope = rememberCoroutineScope()
        val expandBottomSheet = {
            scope.launch {
                bottomSheetState.animateTo(ModalBottomSheetValue.Expanded)
            }
        }
        val hideBottomSheet = {
            scope.launch {
                bottomSheetState.animateTo(ModalBottomSheetValue.Hidden)
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                modifier = Modifier.wrapContentSize(),
                onClick = { expandBottomSheet() }
            ) {
                Text(text = "Open bottom sheet")
            }
            Spacer(modifier = Modifier.size(16.dp))

            Text(
                text = "Selected items: ${state.value.selectedItems}",
                style = MaterialTheme.typography.subtitle1
            )
        }

        MultiSelectWidgetBottomSheetSample(bottomSheetState, state, temporaryState, hideBottomSheet)
    }
}

@ExperimentalMaterialApi
@Composable
private fun MultiSelectWidgetBottomSheetSample(
    bottomSheetState: ModalBottomSheetState,
    state: MutableState<MultiSelectWidget.MultiSelectWidgetState>,
    temporaryState: MutableState<MultiSelectWidget.MultiSelectWidgetState>,
    hideBottomSheet: () -> Job
) {

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetContent = {

            MultiSelectWidgetInBottomSheet(
                modifier = Modifier.padding(16.dp),
                state = temporaryState.value,
                onStateChanged = {
                    temporaryState.value = it
                },
                onSubmit = {
                    state.value = temporaryState.value
                    hideBottomSheet()
                }
            )
        },
        content = {}
    )
}

@Composable
fun MultiSelectWidgetInBottomSheet(
    modifier: Modifier = Modifier,
    state: MultiSelectWidget.MultiSelectWidgetState,
    onStateChanged: (MultiSelectWidget.MultiSelectWidgetState) -> Unit,
    onSubmit: () -> Unit
) {

    MultiSelectWidget.MultiSelectWidget(
        modifier = modifier,
        state = state,
        onStateChanged = { newState ->
            onStateChanged(newState)
        },
        onSubmit = onSubmit,
        onFirstItemSelected = { midState ->
            val firstSelectedItem = midState.selectedItems.first()
            // If Ball is selected, disable ["Apple", "Cat"]
            if (firstSelectedItem == "Ball") {
                val newState = midState.copy(
                    type = MultiSelectWidget.MultiSelectType.DisabledItemsSupport.create(
                        state.items,
                        listOf("Apple", "Cat")
                    )
                )
                onStateChanged(newState)
            } else {
                onStateChanged(midState)
            }
        },
        onNoItemsSelected = { midState ->
            // Clear disabledItems
            val newState = midState.copy(
                type = MultiSelectWidget.MultiSelectType.DisabledItemsSupport.create(
                    state.items,
                    emptyList()
                )
            )
            onStateChanged(newState)
        }
    )
}