package com.streamliners.widgetssample.multiselect.widget

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp

object MultiSelectWidget {

    data class State(
        val items: List<String>,
        val selectedItems: List<String>,
        val type: MultiSelectType = MultiSelectType.Normal
    ) {

        fun addItem(item: String): State = copy(
            selectedItems = selectedItems + item
        )

        fun removeItem(item: String): State = copy(
            selectedItems = selectedItems - item
        )
    }

    sealed class MultiSelectType {

        object Normal : MultiSelectType()

        data class DisabledItemsSupport(
            val itemsToEnabledOrNotMap: Map<String, Boolean>
        ) : MultiSelectType() {

            companion object {
                fun create(
                    items: List<String>,
                    disabledItems: List<String>
                ): DisabledItemsSupport {

                    return DisabledItemsSupport(
                        itemsToEnabledOrNotMap = items.associateWith {
                            !disabledItems.contains(it)
                        }
                    )
                }
            }
        }
    }

    @Composable
    fun MultiSelectWidget(
        modifier: Modifier = Modifier,
        state: State,
        onStateChanged: (State) -> Unit,
        onSubmit: () -> Unit,
        onFirstItemSelected: (State) -> Unit = { onStateChanged(it) },
        onNoItemsSelected: (State) -> Unit = { onStateChanged(it) }
    ) {
        Column(
            modifier = modifier
        ) {

            when (state.type) {
                is MultiSelectType.DisabledItemsSupport -> {
                    state.type.itemsToEnabledOrNotMap
                    state.items.forEach { item ->
                        CheckBox(
                            label = item,
                            checked = state.selectedItems.contains(item),
                            enabled = state.type.itemsToEnabledOrNotMap.getOrDefault(item, true),
                            onCheckChanged = { checked ->
                                val newState =
                                    if (checked)
                                        state.addItem(item)
                                    else
                                        state.removeItem(item)

                                if (newState.selectedItems.isEmpty())
                                    onNoItemsSelected(newState)
                                else if (newState.selectedItems.size == 1)
                                    onFirstItemSelected(newState)
                                else
                                    onStateChanged(newState)
                            }
                        )
                    }
                }
                MultiSelectType.Normal -> {
                    state.items.forEach { item ->
                        CheckBox(
                            label = item,
                            enabled = true,
                            checked = state.selectedItems.contains(item),
                            onCheckChanged = { checked ->
                                val newState =
                                    if (checked)
                                        state.addItem(item)
                                    else
                                        state.removeItem(item)

                                onStateChanged(newState)
                            }
                        )
                    }
                }
            }

            Button(
                modifier = Modifier
                    .padding(8.dp)
                    .align(CenterHorizontally),
                onClick = onSubmit
            ) {
                Text(text = "Submit")
            }
        }
    }

    @Composable
    private fun CheckBox(
        label: String,
        enabled: Boolean = true,
        checked: Boolean = false,
        onCheckChanged: (Boolean) -> Unit
    ) {
        val isChecked = remember {
            mutableStateOf(checked)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    if (enabled && isChecked.value) {
                        isChecked.value = false
                        return@clickable
                    }
                    if (enabled && !isChecked.value) {
                        isChecked.value = true
                        return@clickable
                    }
                }
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isChecked.value,
                enabled = enabled,
                onCheckedChange = { onCheckChanged(it) }

            )
            Spacer(
                Modifier.size(8.dp)
            )

            if (enabled) {
                Text(
                    label
                )
            } else {
                Text(
                    text = label,
                    style = TextStyle(textDecoration = TextDecoration.LineThrough)
                )
            }
        }
    }
}