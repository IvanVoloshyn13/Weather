package voloshyn.android.data.model

import voloshyn.android.data.R

sealed class WeatherTypeModel(
    val weatherType: String,
    val weatherIcon: Int
) {
    object ClearSky : WeatherTypeModel(
        weatherType = "Clear sky",
        weatherIcon = R.drawable.ic_wt_clear_sky
    )

    object MainlyClear : WeatherTypeModel(
        weatherType = "Mainly Clear",
        weatherIcon = R.drawable.ic_wt_sunnycloudy
    )

    object PartlyCloud : WeatherTypeModel(
        weatherType = "Partly Cloud",
        weatherIcon = R.drawable.ic_wt_sunnycloudy
    )

    object Overcast : WeatherTypeModel(
        weatherType = "Overcast",
        weatherIcon = R.drawable.ic_wt_cloudy
    )

    object Foggy : WeatherTypeModel(
        weatherType = "Foggy",
        weatherIcon = R.drawable.ic_wt_verycloudy

    )

    object DepositingRimeFog : WeatherTypeModel(
        weatherType = "Depositing rime fog",
        weatherIcon = R.drawable.ic_wt_verycloudy

    )

    object LightDrizzle : WeatherTypeModel(
        weatherType = "Light drizzle",
        weatherIcon = R.drawable.ic_wt_rainshower
    )

    object ModerateDrizzle : WeatherTypeModel(
        weatherType = "Moderate drizzle",
        weatherIcon = R.drawable.ic_wt_rainshower
    )

    object DenseDrizzle : WeatherTypeModel(
        weatherType = "Dense drizzle",
        weatherIcon = R.drawable.ic_wt_rainshower
    )

    object LightFreezingDrizzle : WeatherTypeModel(
        weatherType = "Slight freezing drizzle",
        weatherIcon = R.drawable.ic_wt_snowyrain
    )

    object DenseFreezingDrizzle : WeatherTypeModel(
        weatherType = "Dense freezing drizzle",
        weatherIcon = R.drawable.ic_wt_snowyrain
    )

    object SlightRain : WeatherTypeModel(
        weatherType = "Slight rain",
        weatherIcon = R.drawable.ic_wt_rainy
    )

    object ModerateRain : WeatherTypeModel(
        weatherType = "Rainy",
        weatherIcon = R.drawable.ic_wt_rainy
    )

    object HeavyRain : WeatherTypeModel(
        weatherType = "Heavy rain",
        weatherIcon = R.drawable.ic_wt_rainy
    )

    object HeavyFreezingRain : WeatherTypeModel(
        weatherType = "Heavy freezing rain",
        weatherIcon = R.drawable.ic_wt_snowyrain
    )

    object SlightSnowFall : WeatherTypeModel(
        weatherType = "Slight snow fall",
        weatherIcon = R.drawable.ic_wt_snowy
    )

    object ModerateSnowFall : WeatherTypeModel(
        weatherType = "Moderate snow fall",
        weatherIcon = R.drawable.ic_wt_heavy_snow
    )

    object HeavySnowFall : WeatherTypeModel(
        weatherType = "Heavy snow fall",
        weatherIcon = R.drawable.ic_wt_heavy_snow
    )

    object SnowGrains : WeatherTypeModel(
        weatherType = "Snow grains",
        weatherIcon = R.drawable.ic_wt_heavy_snow
    )

    object SlightRainShowers : WeatherTypeModel(
        weatherType = "Slight rain showers",
        weatherIcon = R.drawable.ic_wt_rainshower
    )

    object ModerateRainShowers : WeatherTypeModel(
        weatherType = "Moderate rain showers",
        weatherIcon = R.drawable.ic_wt_rainshower
    )

    object ViolentRainShowers : WeatherTypeModel(
        weatherType = "Violent rain showers",
        weatherIcon = R.drawable.ic_wt_rainshower
    )

    object SlightSnowShowers : WeatherTypeModel(
        weatherType = "Light snow showers",
        weatherIcon = R.drawable.ic_wt_snowy
    )

    object HeavySnowShowers : WeatherTypeModel(
        weatherType = "Heavy snow showers",
        weatherIcon = R.drawable.ic_wt_snowy
    )

    object ModerateThunderstorm : WeatherTypeModel(
        weatherType = "Moderate thunderstorm",
        weatherIcon = R.drawable.ic_wt_thunder
    )

    object SlightHailThunderstorm : WeatherTypeModel(
        weatherType = "Thunderstorm with slight hail",
        weatherIcon = R.drawable.ic_wt_rainthunder
    )

    object HeavyHailThunderstorm : WeatherTypeModel(
        weatherType = "Thunderstorm with heavy hail",
        weatherIcon = R.drawable.ic_wt_rainthunder
    )


    companion object {
        fun fromWHO(code: Int): WeatherTypeModel {
            return when (code) {
                0 -> ClearSky
                1 -> MainlyClear
                2 -> PartlyCloud
                3 -> Overcast
                45 -> Foggy
                48 -> DepositingRimeFog
                51 -> LightDrizzle
                53 -> ModerateDrizzle
                55 -> DenseDrizzle
                56 -> LightFreezingDrizzle
                57 -> DenseFreezingDrizzle
                61 -> SlightRain
                63 -> ModerateRain
                65 -> HeavyRain
                66 -> LightFreezingDrizzle
                67 -> HeavyFreezingRain
                71 -> SlightSnowFall
                73 -> ModerateSnowFall
                75 -> HeavySnowFall
                77 -> SnowGrains
                80 -> SlightRainShowers
                81 -> ModerateRainShowers
                82 -> ViolentRainShowers
                85 -> SlightSnowShowers
                86 -> HeavySnowShowers
                95 -> ModerateThunderstorm
                96 -> SlightHailThunderstorm
                99 -> HeavyHailThunderstorm
                else -> ClearSky
            }

        }
    }
}

