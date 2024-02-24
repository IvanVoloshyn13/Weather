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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.imageLoader
import coil.request.ImageRequest
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import voloshyn.android.data.repository.weather.WeatherTypeRepository
import voloshyn.android.domain.model.NetworkStatus
import voloshyn.android.domain.model.weather.DailyForecast
import voloshyn.android.domain.model.weather.HourlyForecast
import voloshyn.android.domain.model.weather.MainWeatherInfo
import voloshyn.android.weather.R
import voloshyn.android.weather.databinding.FragmentWeatherBinding
import voloshyn.android.weather.databinding.WidgetForecastBinding
import voloshyn.android.weather.gpsReceiver.GpsReceiver
import voloshyn.android.weather.gpsReceiver.GpsStatus
import voloshyn.android.weather.networkObserver.NetworkObserver
import voloshyn.android.weather.presentation.fragment.renderSimpleResult
import voloshyn.android.weather.presentation.fragment.viewBinding


@AndroidEntryPoint
class WeatherFragment : Fragment(R.layout.fragment_weather) {
    private val binding by viewBinding<FragmentWeatherBinding>()
    private lateinit var widgetForecastBinding: WidgetForecastBinding
    private val viewModel: WeatherViewModel by viewModels()
    private lateinit var hourlyAdapter: HourlyAdapter
    private lateinit var dailyAdapter: DailyAdapter

    private val imageState = MutableStateFlow("")


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        widgetForecastBinding = WidgetForecastBinding.bind(binding.root)
        observeGpsStatus()
        observeNetworkStatus()
        hourlyAdapter = HourlyAdapter()
        dailyAdapter = DailyAdapter()
        initHourlyRecycler()
        initDailyRecycler()
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
        renderUi()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collectLatest { state ->
                if (!state.isLoading) {
                    updateMainWeatherWidget(state.mainWeatherInfo)
                    updateWidgetForecast(state)
                    //    updateShowMoreLessLocationState(state = state.showMoreLess)
                    binding.apply {
                        with(toolbar) {
                            tvCurrentTime.text = state.currentTime
                            tvToolbarTitle.text = state.location
                        }
                        if (state.backgroundImage.isNotEmpty()) {
                            if (checkForImageUpdate(state.backgroundImage)) {
                                val request = ImageRequest.Builder(requireContext())
                                    .data(state.backgroundImage)
                                    .target {
                                        binding.mainDrawer.background = it
                                    }
                                    .build()
                                requireContext().imageLoader.enqueue(request)
                            }
                        } else {
                            binding.mainDrawer.background =
                                requireContext().getDrawable(R.drawable.splash)
                        }
                        if (state.isLoading) {
                            //     binding.progressBar.visibility = View.VISIBLE
                        } else {
                            //    binding.progressBar.visibility = View.GONE
                        }
                    }
                    // savedLocationAdapter.submitList(state.cities)
                }
            }
        }

    }

    private fun updateWidgetForecast(state: WeatherState) {
        dailyAdapter.submitList(state.dailyForecast as List<DailyForecast>)
        hourlyAdapter.submitList(state.hourlyForecast as List<HourlyForecast>)
    }

    private fun updateMainWeatherWidget(state: MainWeatherInfo) {
        binding.apply {
            tvCurrentTemp.text =
                state.currentTemperature.toString()
            tvMaxTemp.text =
                state.maxTemperature.toString()
            tvMinTemp.text =
                state.minTemperature.toString()
            tvWeatherTypeDesc.text =
                WeatherTypeRepository.fromWHO(state.weatherCode).weatherType
            ivWeatherTypeIcon.setImageResource(WeatherTypeRepository.fromWHO(state.weatherCode).weatherIcon)
        }

    }

    private fun renderUi() {
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

    private fun initHourlyRecycler() {
        widgetForecastBinding.rvHourlyForecast.adapter = hourlyAdapter
        widgetForecastBinding.rvHourlyForecast.layoutManager =
            LinearLayoutManager(
                this@WeatherFragment.requireContext(),
                RecyclerView.HORIZONTAL, false
            )
    }

    private fun initDailyRecycler() {
        widgetForecastBinding.rvDaily.adapter = dailyAdapter
        widgetForecastBinding.rvDaily.layoutManager =
            LinearLayoutManager(
                this@WeatherFragment.requireContext(),
                RecyclerView.VERTICAL, false
            )
    }

    private fun checkForImageUpdate(image: String): Boolean {
        return if (imageState.value != image) {
            imageState.value = image
            true
        } else false
    }

    private fun observeGpsStatus() {
        val fragmentAct = requireActivity() as GpsReceiver
        var gpsUnavailableDialog: GpsUnavailableDialog? = null
        lifecycleScope.launch {
            fragmentAct.gpsStatus.collectLatest {
                when (it) {
                    GpsStatus.AVAILABLE -> {
                        gpsUnavailableDialog?.dialog?.dismiss()
                        gpsUnavailableDialog = null
                        viewModel.onIntent(GetWeatherByCurrentLocation)
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