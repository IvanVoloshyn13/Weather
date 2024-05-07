package voloshyn.android.weather.presentation.fragment.base

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow

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