package voloshyn.android.domain.appError


typealias RootError = AppError

sealed interface AppResult<out D, out E : RootError> {

    class Success<out D, out E : RootError>(val data: D) : AppResult<D, E>
    class Error<out D, out E : RootError>(val data: D? = null, val error: E) : AppResult<D, E>
}