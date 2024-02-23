package voloshyn.android.weather.presentation.fragment.weather

import android.os.Bundle
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import voloshyn.android.domain.model.NetworkStatus
import voloshyn.android.weather.R
import voloshyn.android.weather.databinding.FragmentWeatherBinding
import voloshyn.android.weather.gpsReceiver.GpsReceiver
import voloshyn.android.weather.gpsReceiver.GpsStatus
import voloshyn.android.weather.networkObserver.NetworkObserver
import voloshyn.android.weather.presentation.fragment.renderSimpleResult
import voloshyn.android.weather.presentation.fragment.viewBinding

const val ANDROID_ACTION_BAR = 56

@AndroidEntryPoint
class WeatherFragment : Fragment(R.layout.fragment_weather) {
    private val binding by viewBinding<FragmentWeatherBinding>()
    private val viewModel: WeatherViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeGpsStatus()
        observeNetworkStatus()
        val displayMetrics = requireContext().resources.displayMetrics
        val screenHeight = displayMetrics.heightPixels

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBarInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.layoutWeatherType.updateLayoutParams<MarginLayoutParams> {
                topMargin = measuredCurrentWeatherWidgetMargins(
                    screenHeight = screenHeight,
                    systemBarInsets = systemBarInsets
                )
            }
            insets
        }
        binding.toolbar.mainToolbar.setNavigationOnClickListener {
            binding.mainDrawer.openDrawer(GravityCompat.START)
        }
        updateUi()

    }

    private fun updateUi() {
        lifecycleScope.launch {
            viewModel.state.collectLatest {
                renderSimpleResult(binding.scroll, it.isError, it.isLoading,
                    onLoading = {
                        //      binding.progressBar.visibility = View.VISIBLE
                    }, onError = {
                        binding.errorDialog.errorDialog.visibility = View.VISIBLE
                    }
                ) {
                    binding.errorDialog.errorDialog.visibility = View.GONE
                }
            }
        }
    }


    private fun measuredCurrentWeatherWidgetMargins(
        screenHeight: Int,
        systemBarInsets: androidx.core.graphics.Insets
    ): Int {
        val insetsValue = systemBarInsets.top + systemBarInsets.bottom
        val layoutMeasure = with(binding) {
            layoutWeatherType.measuredHeight + layoutDailyTemp.measuredHeight + tvCurrentTemp.measuredHeight
        }
        return screenHeight - insetsValue - layoutMeasure
    }

    private fun observeGpsStatus() {
        val fragmentAct = requireActivity() as GpsReceiver
        var gpsUnavailableDialog: GpsUnavailableDialog? = null
        lifecycleScope.launch {
            fragmentAct.gpsStatus.collectLatest {
                when (it) {
                    GpsStatus.AVAILABLE -> {
                        viewModel.getCurrentLocation()
                        gpsUnavailableDialog?.dialog?.dismiss()
                        gpsUnavailableDialog = null
                        viewModel.getCurrentLocation()
                    }

                    GpsStatus.UNAVAILABLE -> {
                        gpsUnavailableDialog = GpsUnavailableDialog()
                        gpsUnavailableDialog?.show(childFragmentManager, null)
                    }
                }
            }
        }
    }

    private fun observeNetworkStatus() {
        val fragmentAct = requireActivity() as NetworkObserver
        viewLifecycleOwner.lifecycleScope.launch {
            fragmentAct.networkStatusFlow.collectLatest { network ->
                network?.let {
                    when (it) {
                        NetworkStatus.AVAILABLE -> {
                            Toast.makeText(
                                requireContext(),
                                it.name,
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        NetworkStatus.LOST -> {
                            Toast.makeText(
                                requireContext(),
                                it.name,
                                Toast.LENGTH_SHORT
                            ).show()

                        }

                        NetworkStatus.UNAVAILABLE -> {
                            Toast.makeText(
                                requireContext(),
                                it.name,
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }
                }
            }
        }
    }

}