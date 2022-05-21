package com.streamliners.widgetssample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.streamliners.widgetssample.multiselect.bottomsheet.MultiSelectBottomSheetSample
import com.streamliners.widgetssample.multiselect.widget.MultiSelectWidgetDisabledItemsSupportSample
import com.streamliners.widgetssample.multiselect.widget.MultiSelectWidgetNormalSample
import com.streamliners.widgetssample.other.SampleCard

@ExperimentalMaterialApi
@Composable
fun MultiSelectSample() {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SampleCard(
            modifier = Modifier.padding(8.dp),
            title = "Widget.Normal"
        ) {
            MultiSelectWidgetNormalSample()
        }

        SampleCard(
            modifier = Modifier.padding(8.dp),
            title = "Widget.DisabledItemsSupport"
        ) {
            MultiSelectWidgetDisabledItemsSupportSample()
        }

        SampleCard(
            modifier = Modifier.padding(8.dp),
            title = "BottomSheet.DisabledItemsSupport"
        ) {
            MultiSelectBottomSheetSample()
        }
    }
}