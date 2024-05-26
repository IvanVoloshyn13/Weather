package voloshyn.android.weather.presentation.fragment.weather.mvi

import androidx.annotation.StringRes
import voloshyn.android.weather.R

data class UiEffects(
    val isError: Boolean = false,
    val showToast: Boolean = false,
    val message: Message = Message()
)

data class Message(
    @StringRes val stringRes: Int = R.string.empty_string,
    val message: String = ""
)