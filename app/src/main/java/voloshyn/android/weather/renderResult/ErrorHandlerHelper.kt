package voloshyn.android.weather.renderResult

import voloshyn.android.domain.appError.DataError
import voloshyn.android.domain.appError.LocationProviderError
import voloshyn.android.weather.R

fun LocationProviderError.toStringResources(): Int {
    return when (this) {
        LocationProviderError.NO_PERMISSION -> R.string.no_location_permission
        LocationProviderError.PROVIDER_ERROR -> R.string.error_provider_disabled
        LocationProviderError.NO_LOCATION -> R.string.error_location_retrieve
    }
}

fun DataError.toStringResources():Int{
    return when(this){
        DataError.Locale.LOCAL_STORAGE_ERROR -> R.string.error_sql
        DataError.Locale.DATA_NOT_FOUND -> R.string.error_sql
        DataError.Network.N0_CONNECTION -> R.string.error_no_network
        DataError.Network.CLIENT_ERROR -> R.string.global_network_error_message
        DataError.Network.SERVER_ERROR -> R.string.global_network_error_message
        DataError.Network.UNKNOWN_ERROR -> voloshyn.android.data.R.string.unknown_error
    }
}



