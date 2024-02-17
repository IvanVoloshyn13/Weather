package voloshyn.android.weather.fragment.weather

import android.os.Bundle
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.marginBottom
import androidx.core.view.marginTop
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import voloshyn.android.weather.R
import voloshyn.android.weather.databinding.FragmentWeatherBinding
import voloshyn.android.weather.fragment.viewBinding

const val ANDROID_ACTION_BAR = 56

class WeatherFragment : Fragment(R.layout.fragment_weather) {
    private val binding by viewBinding<FragmentWeatherBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val displayMetrics = requireContext().resources.displayMetrics
        val density = displayMetrics.density
        val screenHeight = displayMetrics.heightPixels

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBarInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.layoutWeatherType.updateLayoutParams<MarginLayoutParams> {
                topMargin = measuredCurrentWeatherWidgetMargins(
                    screenHeight = screenHeight,
                    density = density,
                    systemBarInsets = systemBarInsets
                )
            }
            insets
        }
    }


    private fun measuredCurrentWeatherWidgetMargins(
        screenHeight: Int,
        density: Float,
        systemBarInsets: androidx.core.graphics.Insets
    ): Int {
        val insetsValue = systemBarInsets.top + systemBarInsets.bottom
        val layoutMeasure = with(binding) {
            layoutWeatherType.measuredHeight + layoutDailyTemp.measuredHeight + tvCurrentTemp.measuredHeight
        }
        return screenHeight - (ANDROID_ACTION_BAR * density + 0.5f).toInt() - insetsValue - layoutMeasure
    }
}