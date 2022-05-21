package com.streamliners.widgetssample.multiselect

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

object MultiSelectWidget {

    data class MultiSelectWidgetState(
        val items: List<String>,
        val selectedItems: List<String>,
        val type: MultiSelectType = MultiSelectType.Normal
    ) {

        fun addItem(item: String): MultiSelectWidgetState = copy(
            selectedItems = selectedItems + item
        )

        fun removeItem(item: String): MultiSelectWidgetState = copy(
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
        state: MultiSelectWidgetState,
        onStateChanged: (MultiSelectWidgetState) -> Unit,
        onSubmit: () -> Unit,
        onFirstItemSelected: (MultiSelectWidgetState) -> Unit = { onStateChanged(it) },
        onNoItemsSelected: (MultiSelectWidgetState) -> Unit = { onStateChanged(it) }
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
                                    if(checked)
                                        state.addItem(item)
                                    else
                                        state.removeItem(item)

                                if(newState.selectedItems.isEmpty())
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
                                    if(checked)
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { if (enabled) onCheckChanged(!checked) }
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = checked,
                enabled = enabled,
                onCheckedChange = { }
            )
            Spacer(
                Modifier.size(8.dp)
            )
            Text(
                label
            )
        }
    }
}