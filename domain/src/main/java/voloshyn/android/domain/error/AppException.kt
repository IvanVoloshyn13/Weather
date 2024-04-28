package voloshyn.android.domain.error

sealed class AppException(message:String) : RuntimeException(message)
class UnknownException(message:String) : AppException(message)
class LocalStorageException(message:String) : AppException(message)

sealed class NetworkException(message:String):AppException(message){
    class ClientException(message:String) : AppException(message)
    class ServerException(message:String) : AppException(message)
    class EmptyBodyException(message: String) : AppException(message)
    class NoNetworkException(message:String) : AppException(message)
}

sealed class LocationProviderException(message: String):AppException(message){
    class NoLocationPermissionException(message: String) : AppException(message)
    class NoLocationException(message:String) : AppException(message)
    class ProviderException(message: String):AppException(message)
}
