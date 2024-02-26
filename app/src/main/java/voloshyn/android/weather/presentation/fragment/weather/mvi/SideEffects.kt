package voloshyn.android.weather.presentation.fragment.weather.mvi

data class SideEffects(
    val showErrorMessage: Boolean = false,
    val errorMessage: String = "",
)