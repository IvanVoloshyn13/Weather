package voloshyn.android.domain.appError


typealias RootError = AppError

sealed interface AppResult<out D, out E : RootError> {
    class Success<out D, out E : RootError>(val data: D) : AppResult<D, E>
    class Error<out E : RootError>(val error: E) : AppResult<Nothing, E>

}


