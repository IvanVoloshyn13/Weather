package voloshyn.android.data.mappers

import voloshyn.android.data.dataSource.local.database.entities.PlaceEntity
import voloshyn.android.domain.model.place.Place
import voloshyn.android.network.retrofit.models.search.PlacesSearchResponse
import voloshyn.android.network.retrofit.models.search.SearchedPlaces


fun PlacesSearchResponse.toSearchedCityList(): ArrayList<Place> {
    return this.citiesList.map { location: SearchedPlaces ->
        Place(
            id = location.id,
            name = location.name,
            latitude = location.latitude,
            longitude = location.longitude,
            timezone = location.timezone,
            country = location.country,
            countryCode = location.countyCode
        )
    } as ArrayList<Place>
}

fun Array<Place>.toPlaceEntityArray(): List<PlaceEntity> {
    return this.map {
        PlaceEntity(
            id = it.id,
            name = it.name,
            latitude = it.latitude,
            longitude = it.longitude,
            timezone = it.timezone,
            country = it.country,
            countryCode = it.countryCode
        )
    }
}

 fun PlaceEntity.toPlace(): Place {
    return Place(
        id = id,
        name = name,
        latitude = latitude,
        longitude = longitude,
        timezone = timezone,
        country = country,
        countryCode = countryCode
    )
}

fun Place.toPlaceEntity(): PlaceEntity {
    return PlaceEntity(
        id = id,
        name = name,
        latitude = latitude, longitude = longitude, timezone = timezone, country = country,
        countryCode = countryCode
    )
}