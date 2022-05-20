package com.streamliners.commons.uiEventsBase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movetez.ui.UiEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

open class BaseViewModel: ViewModel() {

    val uiEventFlow = MutableSharedFlow<UiEvent>()

    fun showErrorDialog(
        // Basic
        title: String = "Error",
        message: String = "Something went wrong",
        isCancellable: Boolean = false,

        // Retry
        withRetry: Boolean = false,
        retryButtonLabel: String = "Retry",
        onRetry: () -> Unit = {},

        // Close
        closeButtonLabel: String = "Retry",
        onClose: () -> Unit = {}
    ) { viewModelScope.launch {
        uiEventFlow.emit(UiEvent.ShowErrorDialog(
            title,
            message,
            isCancellable,
            withRetry,
            retryButtonLabel,
            onRetry,
            closeButtonLabel,
            onClose
        ))
    }}

    fun showLoadingDialog(
        withLottie: Boolean = false,
        lottieResource: String? = null
    ) { viewModelScope.launch {
        uiEventFlow.emit(UiEvent.ShowLoadingDialog(withLottie, lottieResource))
    }}

    fun hideLoadingDialog() { viewModelScope.launch {
        uiEventFlow.emit(UiEvent.HideLoadingDialog)
    }}

    fun showToast(msg: String) { viewModelScope.launch {
        uiEventFlow.emit(UiEvent.ShowToast(msg))
    }}

}