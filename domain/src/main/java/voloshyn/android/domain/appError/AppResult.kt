package voloshyn.android.domain.appError


typealias RootError = AppError

sealed interface AppResult<out D, out E : RootError> {
    class Success<out D, out E : RootError>(val data: D) : AppResult<D, E>
    class Error<out D, out E : RootError>(val data: D? = null, val error: E) : AppResult<D, E>

}


suspend fun <D, E : RootError> AppResult.Error<D, E>.mapToData(
    dataBlock: suspend (D) -> Unit,
    errorBlock: suspend (E) -> Unit
) {
    if (this.data != null) {
        dataBlock(data)
        errorBlock(error)

    } else errorBlock(error)
}





