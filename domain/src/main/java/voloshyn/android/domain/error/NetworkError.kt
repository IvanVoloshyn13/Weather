package voloshyn.android.domain.error

sealed interface DataError : AppError {
    enum class Network : DataError {
        N0_CONNECTION, CLIENT_ERROR, EMPTY_BODY, SERVER_ERROR, REQUEST_TIMEOUT,UNKNOWN_ERROR
    }

    enum class Locale:DataError {
        LOCAL_STORAGE_ERROR, DATA_NOT_FOUND
    }
}