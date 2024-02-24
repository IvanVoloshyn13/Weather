package voloshyn.android.data.repository.weather

import voloshyn.android.data.R

sealed class WeatherTypeRepository(
    val weatherType: String,
    val weatherIcon: Int
) {
    object ClearSky : WeatherTypeRepository(
        weatherType = "Clear sky",
        weatherIcon = R.drawable.ic_wt_clear_sky
    )

    object MainlyClear : WeatherTypeRepository(
        weatherType = "Mainly Clear",
        weatherIcon = R.drawable.ic_wt_sunnycloudy
    )

    object PartlyCloud : WeatherTypeRepository(
        weatherType = "Partly Cloud",
        weatherIcon = R.drawable.ic_wt_sunnycloudy
    )

    object Overcast : WeatherTypeRepository(
        weatherType = "Overcast",
        weatherIcon = R.drawable.ic_wt_cloudy
    )

    object Foggy : WeatherTypeRepository(
        weatherType = "Foggy",
        weatherIcon = R.drawable.ic_wt_verycloudy

    )

    object DepositingRimeFog : WeatherTypeRepository(
        weatherType = "Depositing rime fog",
        weatherIcon = R.drawable.ic_wt_verycloudy

    )

    object LightDrizzle : WeatherTypeRepository(
        weatherType = "Light drizzle",
        weatherIcon = R.drawable.ic_wt_rainshower
    )

    object ModerateDrizzle : WeatherTypeRepository(
        weatherType = "Moderate drizzle",
        weatherIcon = R.drawable.ic_wt_rainshower
    )

    object DenseDrizzle : WeatherTypeRepository(
        weatherType = "Dense drizzle",
        weatherIcon = R.drawable.ic_wt_rainshower
    )

    object LightFreezingDrizzle : WeatherTypeRepository(
        weatherType = "Slight freezing drizzle",
        weatherIcon = R.drawable.ic_wt_snowyrain
    )

    object DenseFreezingDrizzle : WeatherTypeRepository(
        weatherType = "Dense freezing drizzle",
        weatherIcon = R.drawable.ic_wt_snowyrain
    )

    object SlightRain : WeatherTypeRepository(
        weatherType = "Slight rain",
        weatherIcon = R.drawable.ic_wt_rainy
    )

    object ModerateRain : WeatherTypeRepository(
        weatherType = "Rainy",
        weatherIcon = R.drawable.ic_wt_rainy
    )

    object HeavyRain : WeatherTypeRepository(
        weatherType = "Heavy rain",
        weatherIcon = R.drawable.ic_wt_rainy
    )

    object HeavyFreezingRain : WeatherTypeRepository(
        weatherType = "Heavy freezing rain",
        weatherIcon = R.drawable.ic_wt_snowyrain
    )

    object SlightSnowFall : WeatherTypeRepository(
        weatherType = "Slight snow fall",
        weatherIcon = R.drawable.ic_wt_snowy
    )

    object ModerateSnowFall : WeatherTypeRepository(
        weatherType = "Moderate snow fall",
        weatherIcon = R.drawable.ic_wt_heavy_snow
    )

    object HeavySnowFall : WeatherTypeRepository(
        weatherType = "Heavy snow fall",
        weatherIcon = R.drawable.ic_wt_heavy_snow
    )

    object SnowGrains : WeatherTypeRepository(
        weatherType = "Snow grains",
        weatherIcon = R.drawable.ic_wt_heavy_snow
    )

    object SlightRainShowers : WeatherTypeRepository(
        weatherType = "Slight rain showers",
        weatherIcon = R.drawable.ic_wt_rainshower
    )

    object ModerateRainShowers : WeatherTypeRepository(
        weatherType = "Moderate rain showers",
        weatherIcon = R.drawable.ic_wt_rainshower
    )

    object ViolentRainShowers : WeatherTypeRepository(
        weatherType = "Violent rain showers",
        weatherIcon = R.drawable.ic_wt_rainshower
    )

    object SlightSnowShowers : WeatherTypeRepository(
        weatherType = "Light snow showers",
        weatherIcon = R.drawable.ic_wt_snowy
    )

    object HeavySnowShowers : WeatherTypeRepository(
        weatherType = "Heavy snow showers",
        weatherIcon = R.drawable.ic_wt_snowy
    )

    object ModerateThunderstorm : WeatherTypeRepository(
        weatherType = "Moderate thunderstorm",
        weatherIcon = R.drawable.ic_wt_thunder
    )

    object SlightHailThunderstorm : WeatherTypeRepository(
        weatherType = "Thunderstorm with slight hail",
        weatherIcon = R.drawable.ic_wt_rainthunder
    )

    object HeavyHailThunderstorm : WeatherTypeRepository(
        weatherType = "Thunderstorm with heavy hail",
        weatherIcon = R.drawable.ic_wt_rainthunder
    )


    companion object {
        fun fromWHO(code: Int): WeatherTypeRepository {
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

