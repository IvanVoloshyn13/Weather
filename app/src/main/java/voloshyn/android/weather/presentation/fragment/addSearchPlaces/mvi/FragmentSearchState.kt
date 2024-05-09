package voloshyn.android.weather.presentation.fragment.addSearchPlaces.mvi

import voloshyn.android.domain.repository.Places

data class FragmentSearchState(
    val isLoading: Boolean = false,
    val places: Places = ArrayList()
)