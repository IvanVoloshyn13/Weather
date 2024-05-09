package voloshyn.android.weather.renderResult

import androidx.annotation.StringRes

data class ErrorState(
    val isError: Boolean = false,
    @StringRes val errorMessage: Int,
)