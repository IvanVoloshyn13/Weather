package voloshyn.android.weather.presentation.fragment

import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import voloshyn.android.weather.R

fun renderSimpleResult(
    root: ViewGroup,
    isError: Boolean,
    isLoading: Boolean,
    onLoading: () -> Unit,
    onError: () -> Unit,
    onSuccess: () -> Unit,

    ) {
    renderResult(
        root = root,
        isError = isError,
        isLoading = isLoading,
        onError = {
            onError()
        },
        onLoading = {
            onLoading()
        },
        onSuccess = {
            root.children.filter { it.id != R.id.error_dialog }
                .forEach { it.visibility = View.VISIBLE }
            onSuccess()
        }
    )
}


private fun renderResult(
    root: ViewGroup, isError: Boolean, isLoading: Boolean,
    onError: () -> Unit,
    onSuccess: () -> Unit,
    onLoading: () -> Unit,
) {
    root.children
        .forEach {
            it.visibility = View.GONE
        }
    when {
        isError -> {
            onError()
        }

        isLoading -> {
            onLoading()
        }

        else -> {
            onSuccess()
        }
    }

}