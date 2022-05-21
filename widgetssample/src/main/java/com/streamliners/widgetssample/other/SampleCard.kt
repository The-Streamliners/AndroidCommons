package com.streamliners.widgetssample.other

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.streamliners.widgetssample.ui.theme.Purple50

@Composable
fun SampleCard(
    modifier: Modifier = Modifier,
    title: String,
    Content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(16.dp))
            .background(Purple50)
            .padding(8.dp)
    ) {
        Text(
            modifier = Modifier.align(CenterHorizontally),
            text = title,
            style = MaterialTheme.typography.h6
        )
        Spacer(Modifier.size(8.dp))
        Content()
    }
}