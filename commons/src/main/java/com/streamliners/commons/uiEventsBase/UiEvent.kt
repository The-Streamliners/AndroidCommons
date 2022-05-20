package com.movetez.ui

sealed class UiEvent {

    class ShowLoadingDialog(
        val withLottie: Boolean = false,
        val lottieResource: String? = null
    ) : UiEvent()

    object HideLoadingDialog: UiEvent()

    class ShowToast(val message: String): UiEvent()

    class ShowErrorDialog(
        // Basic
        val title: String = "Error",
        val message: String = "Something went wrong",
        val isCancellable: Boolean = false,

        // Retry
        val withRetry: Boolean = false,
        val retryButtonLabel: String = "Retry",
        val onRetry: () -> Unit = {},

        // Close
        val closeButtonLabel: String = "Retry",
        val onClose: () -> Unit = {}
    ): UiEvent()
}