package com.streamliners.widgetssample.multiselect.bottomsheet

import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.streamliners.widgetssample.multiselect.widget.MultiSelectWidget
import com.streamliners.widgetssample.multiselect.widget.MultiSelectWidget.MultiSelectWidget

object MultiSelectBottomSheet {

    data class State(
        val widgetState: MultiSelectWidget.State,
        val visible: Boolean
    ) {
        companion object {
            fun create(
                items: List<String>
            ) = State(
                widgetState = MultiSelectWidget.State(
                    items = items,
                    selectedItems = emptyList(),
                    type = MultiSelectWidget.MultiSelectType.DisabledItemsSupport.create(
                        items = items,
                        disabledItems = emptyList()
                    )
                ),
                visible = false
            )
        }

        fun show() = copy(visible = true)

        fun hide() = copy(visible = false)
    }

    @ExperimentalMaterialApi
    @Composable
    fun MultiSelectBottomSheet(
        state: State,
        onSubmit: (State) -> Unit,
        onBottomSheetDismissed: () -> Unit,
        hideBottomSheet: () -> Unit,
        onFirstItemSelected: (MultiSelectWidget.State) -> MultiSelectWidget.State = { it },
        onNoItemsSelected: (MultiSelectWidget.State) -> MultiSelectWidget.State = { it }
    ) {
        val temporaryState = remember {
            mutableStateOf(state.widgetState)
        }

        val bottomSheetState = rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            confirmStateChange = {
                if(it == ModalBottomSheetValue.Hidden)
                    onBottomSheetDismissed()
                true
            }
        )

        LaunchedEffect(key1 = state) {
            temporaryState.value = state.widgetState
        }

        LaunchedEffect(key1 = state.visible) {
            if(state.visible)
                bottomSheetState.animateTo(ModalBottomSheetValue.Expanded)
            else
                bottomSheetState.animateTo(ModalBottomSheetValue.Hidden)
        }

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
                        onSubmit(
                            state.copy(
                                widgetState = temporaryState.value
                            )
                        )
                        hideBottomSheet()
                    },
                    onFirstItemSelected = onFirstItemSelected,
                    onNoItemsSelected = onNoItemsSelected
                )
            },
            content = {}
        )
    }

    @Composable
    private fun MultiSelectWidgetInBottomSheet(
        modifier: Modifier = Modifier,
        state: MultiSelectWidget.State,
        onStateChanged: (MultiSelectWidget.State) -> Unit,
        onSubmit: () -> Unit,
        onFirstItemSelected: (MultiSelectWidget.State) -> MultiSelectWidget.State,
        onNoItemsSelected: (MultiSelectWidget.State) -> MultiSelectWidget.State
    ) {

        MultiSelectWidget(
            modifier = modifier,
            state = state,
            onStateChanged = { newState ->
                onStateChanged(newState)
            },
            onSubmit = onSubmit,
            onFirstItemSelected = { midState ->
                onStateChanged(
                    onFirstItemSelected(midState)
                )
            },
            onNoItemsSelected = { midState ->
                onStateChanged(
                    onNoItemsSelected(midState)
                )
            }
        )
    }

}