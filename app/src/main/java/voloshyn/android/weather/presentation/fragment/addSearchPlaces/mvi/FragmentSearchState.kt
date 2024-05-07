package voloshyn.android.weather.presentation.fragment.addSearchPlaces.mvi

import androidx.annotation.StringRes
import voloshyn.android.domain.repository.addSearch.Places

data class FragmentSearchState(
    val isLoading: Boolean = false,
    val places: Places = ArrayList()
)