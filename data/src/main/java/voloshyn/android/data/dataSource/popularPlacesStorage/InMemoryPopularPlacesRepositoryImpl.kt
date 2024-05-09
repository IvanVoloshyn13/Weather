package voloshyn.android.data.dataSource.popularPlacesStorage

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import voloshyn.android.data.R
import voloshyn.android.domain.model.place.Place
import javax.inject.Inject

class InMemoryPopularPlacesRepositoryImpl @Inject constructor(
    private val context: Context
) {
    private val placesFlow = MutableStateFlow(POPULAR_PLACES)

    companion object {
        private val POPULAR_PLACES = listOf<PopularPlaceData>(
            PopularPlaceData(
                image = R.drawable.barcelona_small,
                name = R.string.barcelona,
                isChecked = true,
                id = 3128760,
                country = "Spain",
                countryCode = "ES",
                latitude = 41.38879,
                longitude = 2.15899,
                timezone = "Europe/ Madrid"
            ),
            PopularPlaceData(
                image = R.drawable.london_small,
                name = R.string.london,
                isChecked = true,
                id = 2643743,
                country = "United Kingdom",
                countryCode = "GB",
                latitude = 51.50853,
                longitude = -0.12574,
                timezone = "Europe/London"
            ),
            PopularPlaceData(
                image = R.drawable.neworlean_small,
                name = R.string.new_orleans,
                id = 4335045,
                country = "United States",
                countryCode = "US",
                latitude = 29.95465,
                longitude = -90.07507,
                timezone = "America/Chicago"
            ),
            PopularPlaceData(
                image = R.drawable.oslo_small,
                name = R.string.oslo,
                id = 3143244,
                country = "Norway",
                countryCode = "NO",
                latitude = 59.91273,
                longitude = 10.74609,
                timezone = "Europe/Oslo"
            ),
            PopularPlaceData(
                image = R.drawable.newyork_small,
                name = R.string.new_york,
                id = 5128581,
                country = "United States",
                countryCode = "US",
                latitude = 40.71427,
                longitude = -74.00597,
                timezone = "America/New_York"
            ),
            PopularPlaceData(
                image = R.drawable.paris_small,
                name = R.string.paris,
                id = 2988507,
                country = "France",
                countryCode = "FR",
                latitude = 48.85341,
                longitude = 2.3488,
                timezone = "Europe/Paris"
            ),
            PopularPlaceData(
                image = R.drawable.sanfrancisco_small,
                name = R.string.san_francisco,
                id = 5391959,
                country = "United States",
                countryCode = "US",
                latitude = 37.77493,
                longitude = -122.41942,
                timezone = "America/Los_Angeles"
            ),
            PopularPlaceData(
                image = R.drawable.sydney_small,
                name = R.string.sydney,
                id = 2147714,
                country = "Australia",
                countryCode = "AU",
                latitude = -33.86785,
                longitude = 151.20732,
                timezone = "Australia/Sydney"
            )
        )

    }

    fun getPopularPlaces(): Flow<List<PopularPlaceData>> {
        return placesFlow
    }


    fun toDomainPopularPlace(data: PopularPlaceData): Place {
        return Place(
            id = data.id,
            name = context.getString(data.name),
            country = data.country,
            countryCode = data.countryCode,
            latitude = data.latitude,
            longitude = data.longitude,
            timezone = data.timezone
        )

    }

}