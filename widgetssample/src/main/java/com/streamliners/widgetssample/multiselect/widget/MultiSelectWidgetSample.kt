package com.streamliners.widgetssample.multiselect.widget

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun MultiSelectWidgetNormalSample() {
    val state = remember {
        mutableStateOf(
            MultiSelectWidget.State(
                items = listOf("Apple", "Ball", "Cat", "Dog"),
                selectedItems = listOf("Apple"),
                type = MultiSelectWidget.MultiSelectType.Normal
            )
        )
    }

    MultiSelectWidget.MultiSelectWidget(
        state = state.value,
        onStateChanged = { newState ->
            state.value = newState
        },
        onSubmit = {
            Log.v("MultiSelectWidget", "////// List submitted: ${state.value.selectedItems}")
        }
    )
}

@Composable
fun MultiSelectWidgetDisabledItemsSupportSample(
    modifier: Modifier = Modifier
) {
    val items = remember {
        listOf("Apple", "Ball", "Cat", "Dog")
    }
    val state = remember {
        mutableStateOf(
            MultiSelectWidget.State(
                items = items,
                selectedItems = listOf("Apple"),
                type = MultiSelectWidget.MultiSelectType.DisabledItemsSupport.create(
                    items = items,
                    disabledItems = listOf("Cat")
                )
            )
        )
    }

    MultiSelectWidget.MultiSelectWidget(
        modifier = modifier,
        state = state.value,
        onStateChanged = { newState ->
            state.value = newState
        },
        onSubmit = {
            Log.v("MultiSelectWidget", "////// List submitted: ${state.value.selectedItems}")
        },
        onFirstItemSelected = {
            // If Ball is selected, disable ["Apple", "Cat"]
            if (it.selectedItems.first() == "Ball") {
                state.value = state.value.copy(
                    type = MultiSelectWidget.MultiSelectType.DisabledItemsSupport.create(
                        state.value.items,
                        listOf("Apple", "Cat")
                    )
                )
            }
        },
        onNoItemsSelected = {
            // Clear disabledItems
            state.value = state.value.copy(
                type = MultiSelectWidget.MultiSelectType.DisabledItemsSupport.create(
                    state.value.items,
                    emptyList()
                )
            )
        }
    )
}
