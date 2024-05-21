package voloshyn.android.domain.appError

 sealed interface DataError : AppError {
     enum class Network : DataError {
         N0_CONNECTION, CLIENT_ERROR, SERVER_ERROR, UNKNOWN_ERROR
     }

     enum class Locale : DataError {
         LOCAL_STORAGE_ERROR, DATA_NOT_FOUND
     }
 }
