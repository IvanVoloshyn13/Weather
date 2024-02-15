package voloshyn.android.data.popularPlacesStorage

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class PopularPlace(
    @DrawableRes val image: Int,
    @StringRes val name: Int,
    val isChecked: Boolean=false
)
