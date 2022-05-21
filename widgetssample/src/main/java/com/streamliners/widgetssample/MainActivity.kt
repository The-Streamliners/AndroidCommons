package com.streamliners.widgetssample

import android.os.Bundle
import android.util.Log
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
                    selectedItems = listOf("Apple"),
                    type = MultiSelectWidget.MultiSelectType.DisabledItemsSupport.create(
                        items = items,
                        disabledItems = listOf("Cat")
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

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                modifier = Modifier.wrapContentSize(),
                onClick = {
                    scope.launch {
                        bottomSheetState.animateTo(ModalBottomSheetValue.Expanded)
                    }
                }
            ) {
                Text(text = "Open bottom sheet")
            }
        }

        MultiSelectWidgetBottomSheetSample(bottomSheetState, state, temporaryState)
    }
}

@ExperimentalMaterialApi
@Composable
private fun MultiSelectWidgetBottomSheetSample(
    bottomSheetState: ModalBottomSheetState,
    state: MutableState<MultiSelectWidget.MultiSelectWidgetState>,
    temporaryState: MutableState<MultiSelectWidget.MultiSelectWidgetState>
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
        onFirstItemSelected = {
            // If Ball is selected, disable ["Apple", "Cat"]
            if (it == "Ball") {
                val newState = state.copy(
                    type = MultiSelectWidget.MultiSelectType.DisabledItemsSupport.create(
                        state.items,
                        listOf("Apple", "Cat")
                    )
                )
                onStateChanged(newState)
                Log.v("MyLogg", "onFirstItemSelected ////// TempStateChanged: $newState")
            }
        },
        onNoItemsSelected = {
            // Clear disabledItems
            val newState = state.copy(
                type = MultiSelectWidget.MultiSelectType.DisabledItemsSupport.create(
                    state.items,
                    emptyList()
                )
            )
            onStateChanged(newState)
            Log.v("MyLogg", "onNoItemsSelected ////// TempStateChanged: $newState")
        }
    )
}