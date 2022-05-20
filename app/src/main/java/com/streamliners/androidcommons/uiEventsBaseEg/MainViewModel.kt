package com.streamliners.androidcommons.uiEventsBaseEg

import androidx.lifecycle.viewModelScope
import com.streamliners.commons.uiEventsBase.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel: BaseViewModel() {
    init {
        viewModelScope.launch {
            delay(2000)
            showLoadingDialog()

            delay(5000)
            hideLoadingDialog()
            showLoadingDialog(true, "lottie_loading.json")

            delay(5000)
            showErrorDialog()
        }
    }
}