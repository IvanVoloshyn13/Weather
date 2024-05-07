package voloshyn.android.weather.presentation.fragment.onBoarding.first

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import voloshyn.android.weather.R
import voloshyn.android.weather.databinding.FragmentOnboardingFirstBinding
import voloshyn.android.weather.databinding.ItemEverydayWeatherBinding
import voloshyn.android.weather.databinding.ItemTemperatureChangesBinding
import voloshyn.android.weather.databinding.ItemUpcomingRainfallBinding
import voloshyn.android.weather.databinding.ItemWeatherAlertBinding
import voloshyn.android.weather.presentation.fragment.base.viewBinding

@AndroidEntryPoint
class OnBoardingFragmentFirst : Fragment(R.layout.fragment_onboarding_first) {
    private val binding by viewBinding<FragmentOnboardingFirstBinding>()
    private val viewModel: OnBoardingViewModel by viewModels()
    private lateinit var bttEnableNotificationStatus: MutableStateFlow<NotificationButtonStatus>


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

        bttEnableNotificationStatus = MutableStateFlow(
            NotificationButtonStatus(
                isEverydayWeatherNotificationEnabled = isEverydayWeatherNotificationEnabled,
                isUpcomingRainfallNotificationEnabled = isUpcomingRainfallNotificationEnabled,
                isTemperatureChangesNotificationEnabled = isTemperatureChangesNotificationEnabled,
                isWeatherAlertNotificationEnabled = isWeatherAlertNotificationEnabled
            )
        )


        viewLifecycleOwner.lifecycleScope.launch {
            bttEnableNotificationStatus.collect {
                binding.bttEnableNotifications.isEnabled = it.status
                if (!binding.bttEnableNotifications.isEnabled) {
                    binding.bttEnableNotifications.setBackgroundColor(Color.alpha(100))
                }
            }
        }


        everydayWeatherBinding.checkboxEverydayWeather.setOnCheckedChangeListener { _, isChecked ->
            isEverydayWeatherNotificationEnabled = isChecked
            bttEnableNotificationStatus.update { status ->
                status.copy(isEverydayWeatherNotificationEnabled = isChecked)
            }
        }
        temperatureChangesBinding.checkboxTemperatureChanges.setOnCheckedChangeListener { _, isChecked ->
            isTemperatureChangesNotificationEnabled = isChecked
            bttEnableNotificationStatus.update { status ->
                status.copy(isTemperatureChangesNotificationEnabled = isChecked)
            }
        }
        upcomingRainfallBinding.checkboxUpcomingRainfall.setOnCheckedChangeListener { _, isChecked ->
            isUpcomingRainfallNotificationEnabled = isChecked
            bttEnableNotificationStatus.update { status ->
                status.copy(isUpcomingRainfallNotificationEnabled = isChecked)
            }
        }
        weatherAlertBinding.checkboxWeatherAlert.setOnCheckedChangeListener { _, isChecked ->
            isWeatherAlertNotificationEnabled = isChecked
            bttEnableNotificationStatus.update { status ->
                status.copy(isWeatherAlertNotificationEnabled = isChecked)
            }
        }

        binding.bttEnableNotifications.setOnClickListener {
            saveNotificationSettingWithNavigation(
                isEverydayWeatherNotificationEnabled,
                isUpcomingRainfallNotificationEnabled,
                isTemperatureChangesNotificationEnabled,
                isWeatherAlertNotificationEnabled
            )
        }

        binding.bttDisableNotifications.setOnClickListener {
            everydayWeatherBinding.checkboxEverydayWeather.isChecked = false
            temperatureChangesBinding.checkboxTemperatureChanges.isChecked = false
            upcomingRainfallBinding.checkboxUpcomingRainfall.isChecked = false
            weatherAlertBinding.checkboxWeatherAlert.isChecked = false

            saveNotificationSettingWithNavigation(
                isEverydayWeatherNotificationEnabled,
                isUpcomingRainfallNotificationEnabled,
                isTemperatureChangesNotificationEnabled,
                isWeatherAlertNotificationEnabled
            )
        }
    }

    private fun saveNotificationSettingWithNavigation(
        isEverydayWeatherNotificationEnabled: Boolean,
        isUpcomingRainfallNotificationEnabled: Boolean,
        isTemperatureChangesNotificationEnabled: Boolean,
        isWeatherAlertNotificationEnabled: Boolean
    ) {
        lifecycleScope.launch {
            viewModel.savePushNotificationSettings(
                everydayWeather = isEverydayWeatherNotificationEnabled,
                upcomingRainfall = isUpcomingRainfallNotificationEnabled,
                temperatureChanges = isTemperatureChangesNotificationEnabled,
                weatherAlert = isWeatherAlertNotificationEnabled
            )
        }


        val directions =
            OnBoardingFragmentFirstDirections.actionFirstOnBoardingFragmentToSecondOnBoardingFragment()
        findNavController().navigate(directions,
            navOptions {
                anim {
                    enter = R.anim.enter
                    exit = R.anim.exit
                }
                popUpTo(R.id.firstOnBoardingFragment) {
                    inclusive = true
                }
            }
        )

    }


}