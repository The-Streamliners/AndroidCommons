package com.streamliners.widgetssample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.streamliners.widgetssample.multiselect.bottomsheet.MultiSelectBottomSheetSample
import com.streamliners.widgetssample.other.SampleCard
import com.streamliners.widgetssample.ui.theme.CommonsTheme

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
        // MultiSelectSample()
        MultiSelectBottomSheetSample()
    }
}