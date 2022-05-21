package com.streamliners.widgetssample.multiselect

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MultiSelectWidgetSample() {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            modifier = Modifier.align(CenterHorizontally),
            text = "MultiSelectWidget.Normal",
            style = MaterialTheme.typography.h6
        )
        Spacer(Modifier.size(8.dp))
        MultiSelectWidgetNormalSample()

        Divider(Modifier.padding(vertical = 12.dp))

        Text(
            modifier = Modifier.align(CenterHorizontally),
            text = "MultiSelectWidget.DisabledItemsSupport",
            style = MaterialTheme.typography.h6
        )
        Spacer(Modifier.size(8.dp))
        MultiSelectWidgetDisabledItemsSupportSample()
    }
}

@Composable
fun MultiSelectWidgetNormalSample() {
    val state = remember {
        mutableStateOf(
            MultiSelectWidget.MultiSelectWidgetState(
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
            Log.v("MultiSelectDialog", "////// List submitted: ${state.value.selectedItems}")
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

    MultiSelectWidget.MultiSelectWidget(
        modifier = modifier,
        state = state.value,
        onStateChanged = { newState ->
            state.value = newState
        },
        onSubmit = {
            Log.v("MultiSelectDialog", "////// List submitted: ${state.value.selectedItems}")
        },
        onFirstItemSelected = {
            // If Ball is selected, disable ["Apple", "Cat"]
            if (it == "Ball") {
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
