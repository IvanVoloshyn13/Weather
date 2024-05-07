package voloshyn.android.weather.presentation.fragment.base

import androidx.annotation.StringRes

data class ErrorState(
    val isError: Boolean = false,
    @StringRes val errorMessage: Int,
)