package com.streamliners.commons.uiEventsBase

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.movetez.ui.UiEvent
import com.streamliners.commons.R
import com.streamliners.commons.databinding.DialogLoadingBinding
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

open class BaseActivity: ComponentActivity() {

    private var loadingDialog: Dialog? = null

    protected fun collectUiEvents(flow: MutableSharedFlow<UiEvent>) {
        lifecycleScope.launch {
            flow.collect {
                when(it) {
                    is UiEvent.ShowLoadingDialog -> {
                        showLoadingDialog(it.withLottie, it.lottieResource)
                    }
                    is UiEvent.HideLoadingDialog -> {
                        hideLoadingDialog()
                    }
                    is UiEvent.ShowToast -> {
                        Toast.makeText(this@BaseActivity, it.message, Toast.LENGTH_SHORT).show()
                    }
                    is UiEvent.ShowErrorDialog -> {
                        it.apply {
                            showErrorDialog(
                                title,
                                message,
                                isCancellable,
                                withRetry,
                                retryButtonLabel,
                                onRetry,
                                closeButtonLabel,
                                onClose
                            )
                        }

                    }
                }
            }
        }
    }

    /**
     * Shows a simple error dialog with the following information
     * @param title defaults to "Error"
     * @param message defaults to "Something went wrong"
     * @param isCancellable whether the dialog is cancellable
     * @param withRetry whether to show a retry button
     * @param retryButtonLabel label for retry button
     * @param onRetry lambda to be executed on retry button click
     * @param closeButtonLabel label for close button
     * @param onClose lambda to be executed on close button click
     */
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
    ) {
        val builder = MaterialAlertDialogBuilder(this)
            .setTitle(title)
            .setMessage(message)
            .setNegativeButton(closeButtonLabel) { _, _ -> onClose() }
            .setCancelable(isCancellable)

        if(withRetry)
            builder.setPositiveButton(retryButtonLabel) { _, _ -> onRetry() }

        builder.show()
    }

    /**
     * To show a simple loading dialog.
     * Without lottie, a simple circular progress indicator is shown
     * @param withLottie whether to show a lottie animation
     * @param lottieResource resource id of lottie animation
     */
    fun showLoadingDialog(
        withLottie: Boolean = false,
        lottieResource: String? = null
    ) {
        val binding = DialogLoadingBinding.inflate(layoutInflater)

        if (isLoadingDialogShowing())
            return

        if(withLottie && lottieResource != null) {
            binding.lottieView.visibility = VISIBLE
            binding.progressIndicator.visibility = GONE
            binding.lottieView.setAnimation(lottieResource)
        }

        loadingDialog = Dialog(this, R.style.LoaderStyle)
        loadingDialog!!.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(binding.root)
            setCancelable(false)
        }

        if (loadingDialog!!.window == null) return

        loadingDialog!!.window!!.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }

        loadingDialog!!.show()
    }

    /**
     * To hide the loading dialog
     */
    fun hideLoadingDialog() {
        loadingDialog?.dismiss()
        loadingDialog = null
    }

    /**
     * Return true if dialog is shown else false
     */
    private fun isLoadingDialogShowing() : Boolean{
        return loadingDialog?.isShowing ?: false
    }
}