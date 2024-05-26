package voloshyn.android.weather.presentation.fragment

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import voloshyn.android.weather.renderResult.BaseUiEffects

open class BaseViewModel : ViewModel() {


    protected val baseUiEffects = MutableSharedFlow<BaseUiEffects>()

    protected suspend fun emitErrorAndResetStatus(stringResource: Int) {
        baseUiEffects.emit(
            BaseUiEffects(
                isError = true,
                errorMessage = stringResource
            )
        )

        delay(1000)
        baseUiEffects.emit(
            BaseUiEffects(
                isError = false,
                errorMessage = stringResource
            )
        )
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.d("EXCEPTION_HANDLER", "$throwable,  ${throwable.message}")
        return@CoroutineExceptionHandler
    }

    protected val viewModelScope = CoroutineScope(SupervisorJob() + exceptionHandler)


    protected suspend fun onTryAgain(block: suspend () -> Unit) {
        block()
    }

}