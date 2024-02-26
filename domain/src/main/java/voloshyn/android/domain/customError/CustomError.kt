package voloshyn.android.domain.customError

sealed class CustomError( message: String):Exception(message)
class EmptyBodyError( message: String="No found data") : CustomError(message)
class NoLocationPermissionError( message: String="Check location permission"):CustomError(message)
class LocationProviderError( message:String="Check Gps or Network settings") :CustomError(message)
class ClientError( message:String="The requested resource was not found") :CustomError(message)
class ServerError( message:String="An unexpected error occurred on the server") :CustomError(message)
class NoNetworkError( message:String="Check your network connection") :CustomError(message)
class UnknownError( message:String="Unknown error") :CustomError(message)
class IOError( message:String="IO error") :CustomError(message)
