package voloshyn.android.data.popularPlacesStorage

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import voloshyn.android.data.R
import javax.inject.Inject

class InMemoryPopularPlacesRepositoryImpl @Inject constructor() {
    private val placesFlow = MutableStateFlow(POPULAR_PLACES)

    companion object {
        private val POPULAR_PLACES = listOf<PopularPlace>(
            PopularPlace(R.drawable.barcelona_small, R.string.barcelona, isChecked = true),
            PopularPlace(R.drawable.london_small, R.string.london, isChecked = true),
            PopularPlace(R.drawable.neworlean_small, R.string.new_orleans),
            PopularPlace(R.drawable.oslo_small, R.string.oslo),
            PopularPlace(R.drawable.newyork_small, R.string.new_york),
            PopularPlace(R.drawable.paris_small, R.string.paris),
            PopularPlace(R.drawable.sanfrancisco_small, R.string.san_francisco),
            PopularPlace(R.drawable.sydney_small, R.string.sydney),
        )

    }

    fun getPopularPlaces(): Flow<List<PopularPlace>> {
        return placesFlow
    }


}