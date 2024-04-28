package voloshyn.android.domain.error


enum class LocationProviderError:AppError {
    NO_PERMISSION,PROVIDER_ERROR,NO_LOCATION
}