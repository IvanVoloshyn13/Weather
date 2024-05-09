package voloshyn.android.weather.presentation.fragment

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import voloshyn.android.weather.renderResult.ErrorState

open class BaseViewModel : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.d("EXCEPTION_HANDLER", "$throwable,  ${throwable.message}")
        return@CoroutineExceptionHandler
    }

    protected val viewModelScope = CoroutineScope(SupervisorJob() + exceptionHandler)

    protected val baseErrorState = MutableSharedFlow<ErrorState>()

    protected suspend fun emitErrorAndResetStatus(stringResource: Int) {
        baseErrorState.emit(
            ErrorState(
                isError = true,
                errorMessage = stringResource
            )
        )
        delay(1000)
        baseErrorState.emit(
            ErrorState(
                isError = false,
                errorMessage = stringResource
            )
        )
    }

    protected suspend fun onTryAgain(block: suspend () -> Unit) {
        block()
    }

}