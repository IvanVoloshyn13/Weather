package voloshyn.android.data.popularPlacesStorage

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import voloshyn.android.domain.model.onBoarding.PopularPlace

data class PopularPlaceData(
    @DrawableRes val image: Int,
    @StringRes val name: Int,
    val isChecked: Boolean=false,
    val id: Int,
    val country: String,
    val countryCode: String,
    val latitude: Double,
    val longitude: Double,
    val timezone: String
)


