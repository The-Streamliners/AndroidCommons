package com.streamliners.widgetssample.multiselect.bottomsheet

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.streamliners.widgetssample.multiselect.bottomsheet.MultiSelectBottomSheet.MultiSelectBottomSheet
import com.streamliners.widgetssample.multiselect.widget.MultiSelectWidget

@ExperimentalMaterialApi
@Composable
fun MultiSelectBottomSheetSample() {
    val items = remember {
        listOf("Apple", "Ball", "Cat", "Dog")
    }
    val state = remember {
        mutableStateOf(
            MultiSelectBottomSheet.State.create(items)
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            modifier = Modifier.wrapContentSize(),
            onClick = {
                state.value = state.value.show()
            }
        ) {
            Text(text = "Select items")
        }
        Spacer(Modifier.size(16.dp))

        Text(
            text = "Selected items: ${state.value.widgetState.selectedItems}",
            style = MaterialTheme.typography.subtitle1
        )
    }

    MultiSelectBottomSheet(
        state = state.value,
        onSubmit = {
            state.value = it
        },
        onBottomSheetDismissed = {
            state.value = state.value.hide()
        },
        hideBottomSheet = {
            state.value = state.value.hide()
        },
        onFirstItemSelected = { midState ->
            val firstSelectedItem = midState.selectedItems.first()

            // If Ball is selected, disable ["Apple", "Cat"]
            if (firstSelectedItem == "Ball") {
                val newState = midState.copy(
                    type = MultiSelectWidget.MultiSelectType.DisabledItemsSupport.create(
                        items = items,
                        disabledItems = listOf("Apple", "Cat")
                    )
                )
                newState
            } else {
                midState
            }
        },
        onNoItemsSelected = { midState ->
            midState.copy(
                type = MultiSelectWidget.MultiSelectType.DisabledItemsSupport.create(
                    items = items,
                    disabledItems = emptyList()
                )
            )
        }
    )
}