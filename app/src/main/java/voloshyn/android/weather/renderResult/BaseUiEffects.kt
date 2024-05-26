package voloshyn.android.weather.renderResult

import androidx.annotation.StringRes
import voloshyn.android.weather.R

data class BaseUiEffects(
    val isError: Boolean = false,
    @StringRes val errorMessage: Int = R.string.empty_string
)