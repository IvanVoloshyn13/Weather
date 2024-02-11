package voloshyn.android.weather.fragment.onBoarding

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import voloshyn.android.domain.model.onBoarding.PushNotificationSettings
import voloshyn.android.domain.model.onBoarding.toPushNotificationStatus
import voloshyn.android.weather.R
import voloshyn.android.weather.databinding.FragmentOnboardingFirstBinding
import voloshyn.android.weather.databinding.ItemEverydayWeatherBinding
import voloshyn.android.weather.databinding.ItemTemperatureChangesBinding
import voloshyn.android.weather.databinding.ItemUpcomingRainfallBinding
import voloshyn.android.weather.databinding.ItemWeatherAlertBinding
import voloshyn.android.weather.viewBinding

@AndroidEntryPoint
class FirstOnBoardingFragment : Fragment(R.layout.fragment_onboarding_first) {
    private val binding by viewBinding<FragmentOnboardingFirstBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val everydayWeatherBinding = ItemEverydayWeatherBinding.bind(binding.root)
        val temperatureChangesBinding = ItemTemperatureChangesBinding.bind(binding.root)
        val upcomingRainfallBinding = ItemUpcomingRainfallBinding.bind(binding.root)
        val weatherAlertBinding = ItemWeatherAlertBinding.bind(binding.root)

        var isEverydayWeatherNotificationEnabled: Boolean =
            everydayWeatherBinding.checkboxEverydayWeather.isChecked
        var isTemperatureChangesNotificationEnabled: Boolean =
            temperatureChangesBinding.checkboxTemperatureChanges.isChecked
        var isUpcomingRainfallNotificationEnabled: Boolean =
            upcomingRainfallBinding.checkboxUpcomingRainfall.isChecked
        var isWeatherAlertNotificationEnabled: Boolean =
            weatherAlertBinding.checkboxWeatherAlert.isChecked

        everydayWeatherBinding.checkboxEverydayWeather.setOnCheckedChangeListener { _, isChecked ->
            isEverydayWeatherNotificationEnabled = isChecked
        }
        temperatureChangesBinding.checkboxTemperatureChanges.setOnCheckedChangeListener { _, isChecked ->
            isTemperatureChangesNotificationEnabled = isChecked
        }
        upcomingRainfallBinding.checkboxUpcomingRainfall.setOnCheckedChangeListener { _, isChecked ->
            isUpcomingRainfallNotificationEnabled = isChecked
        }
        weatherAlertBinding.checkboxWeatherAlert.setOnCheckedChangeListener { _, isChecked ->
            isWeatherAlertNotificationEnabled = isChecked
        }

        binding.bttEnableNotifications.setOnClickListener {
            val showNotifications = PushNotificationSettings(
                isWeatherAlertNotificationEnabled.toPushNotificationStatus(),
                isUpcomingRainfallNotificationEnabled.toPushNotificationStatus(),
                isEverydayWeatherNotificationEnabled.toPushNotificationStatus(),
                isTemperatureChangesNotificationEnabled.toPushNotificationStatus()
            )
            Toast.makeText(
                requireContext(),
                "${showNotifications.showNotifications}",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.bttDisableNotifications.setOnClickListener {
            everydayWeatherBinding.checkboxEverydayWeather.isChecked=false
            temperatureChangesBinding.checkboxTemperatureChanges.isChecked=false
            upcomingRainfallBinding.checkboxUpcomingRainfall.isChecked=false
            weatherAlertBinding.checkboxWeatherAlert.isChecked=false
        }


    }
}