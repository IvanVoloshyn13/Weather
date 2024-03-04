package voloshyn.android.domain.customError

sealed class CustomError(message: String) : Exception(message)
class EmptyBodyError(message: String) : CustomError(message)
class NoLocationPermissionError(message: String) : CustomError(message)
class LocationProviderError(message: String) : CustomError(message)
class ClientError(message: String) : CustomError(message)
class ServerError(message: String) : CustomError(message)
class NoNetworkError(message: String) : CustomError(message)
class UnknownError(message: String) : CustomError(message)
class IOError(message: String) : CustomError(message)
