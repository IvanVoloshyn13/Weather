package voloshyn.android.weather.renderResult

import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import voloshyn.android.weather.R

suspend fun renderSimpleResult(
    root: ViewGroup,
    isError: Boolean,
    isLoading: Boolean,
    onLoading: () -> Unit,
    onError: suspend () -> Unit,
    onSuccess: suspend () -> Unit,

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


private suspend fun renderResult(
    root: ViewGroup, isError: Boolean, isLoading: Boolean,
    onError: suspend () -> Unit,
    onSuccess: suspend () -> Unit,
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