package voloshyn.android.weather.presentation.fragment.weather

import android.os.Bundle
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.imageLoader
import coil.request.ImageRequest
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import voloshyn.android.data.repository.weather.WeatherTypeRepository
import voloshyn.android.domain.model.NetworkStatus
import voloshyn.android.domain.model.weather.DailyForecast
import voloshyn.android.domain.model.weather.HourlyForecast
import voloshyn.android.domain.model.weather.MainWeatherInfo
import voloshyn.android.weather.R
import voloshyn.android.weather.databinding.FragmentWeatherBinding
import voloshyn.android.weather.databinding.HeaderLayoutBinding
import voloshyn.android.weather.databinding.ProgressBarBinding
import voloshyn.android.weather.databinding.WidgetForecastBinding
import voloshyn.android.weather.gpsReceiver.GpsReceiver
import voloshyn.android.weather.gpsReceiver.GpsStatus
import voloshyn.android.weather.networkObserver.NetworkObserver
import voloshyn.android.weather.presentation.fragment.renderSimpleResult
import voloshyn.android.weather.presentation.fragment.viewBinding
import voloshyn.android.weather.presentation.fragment.weather.adapter.DailyAdapter
import voloshyn.android.weather.presentation.fragment.weather.adapter.HourlyAdapter
import voloshyn.android.weather.presentation.fragment.weather.mvi.GetWeatherByCurrentLocation
import voloshyn.android.weather.presentation.fragment.weather.mvi.UpdateGpsStatus
import voloshyn.android.weather.presentation.fragment.weather.mvi.UpdateNetworkStatus
import voloshyn.android.weather.presentation.fragment.weather.mvi.WeatherState


@AndroidEntryPoint
class WeatherFragment : Fragment(R.layout.fragment_weather) {
    private val binding by viewBinding<FragmentWeatherBinding>()
    private lateinit var widgetForecastBinding: WidgetForecastBinding
    private lateinit var headerBinding: HeaderLayoutBinding
    private lateinit var progressBarBinding: ProgressBarBinding
    private lateinit var drawerLayout: DrawerLayout
    private val viewModel: WeatherViewModel by viewModels()
    private lateinit var hourlyAdapter: HourlyAdapter
    private lateinit var dailyAdapter: DailyAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        widgetForecastBinding = WidgetForecastBinding.bind(binding.root)
        progressBarBinding = ProgressBarBinding.bind(binding.root)
        drawerLayout = binding.mainDrawer
        val header = binding.mainNavView.getHeaderView(0)
        headerBinding = HeaderLayoutBinding.bind(header)
        hourlyAdapter = HourlyAdapter()
        dailyAdapter = DailyAdapter()
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
        initDailyRecycler()
        observeGpsStatus()
        observeNetworkStatus()
        initHourlyRecycler()
        renderUi()
        lifecycleScope.launch {
            viewModel.state.collectLatest { state ->
                updateMainWeatherWidget(state.mainWeatherInfo)
                updateWidgetForecast(state)
                //    updateShowMoreLessLocationState(state = state.showMoreLess)
                binding.apply {
                    with(toolbar) {
                        tvCurrentTime.text = state.currentTime
                        tvToolbarTitle.text = state.location
                    }
                    if (state.backgroundImage.isNotEmpty()) {
                        val request = ImageRequest.Builder(requireContext())
                            .data(state.backgroundImage)
                            .target {
                                mainDrawer.background = it
                            }
                            .build()
                        requireContext().imageLoader.enqueue(request)
                    } else mainDrawer.background =
                        AppCompatResources.getDrawable(requireContext(), R.drawable.splash)
                    // savedLocationAdapter.submitList(state.cities)

                }
            }
        }

        lifecycleScope.launch {
            viewModel.sideEffects.collectLatest {
                if (it.showErrorMessage) {
                    Toast.makeText(requireContext(), it.errorMessage, Toast.LENGTH_SHORT)
                        .show()

                }
            }
        }

        binding.toolbar.mainToolbar.setNavigationOnClickListener {
            binding.mainDrawer.openDrawer(GravityCompat.START)
        }
        headerBinding.currentLocation.cityLayout.setOnClickListener {
            viewModel.onIntent(GetWeatherByCurrentLocation)
            drawerLayout.close()
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
            viewModel.state.collectLatest { state ->
                renderSimpleResult(binding.scroll,
                    isError = state.isError,
                    isLoading = state.isLoading,
                    onLoading = {
                        progressBarBinding.progressBar.visibility = View.VISIBLE
                        binding.errorDialog.errorDialog.visibility = View.GONE
                    },
                    onError = {
                        binding.errorDialog.errorDialog.visibility = View.VISIBLE
                        binding.errorDialog.tvError.text = state.errorMessage
                        progressBarBinding.progressBar.visibility = View.GONE
                    }
                ) {
                    binding.errorDialog.errorDialog.visibility = View.GONE
                    progressBarBinding.progressBar.visibility = View.GONE
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


    private fun observeGpsStatus() {
        val fragmentAct = requireActivity() as GpsReceiver
        var gpsUnavailableDialog: GpsUnavailableDialog? = null
        lifecycleScope.launch {
            fragmentAct.gpsStatus.collectLatest { status ->
                status?.let {
                    when (it) {
                        GpsStatus.AVAILABLE -> {
                            gpsUnavailableDialog?.dialog?.dismiss()
                            gpsUnavailableDialog = null
                            viewModel.onIntent(UpdateGpsStatus(it))
                        }

                        GpsStatus.UNAVAILABLE -> {
                            gpsUnavailableDialog = GpsUnavailableDialog()
                            gpsUnavailableDialog?.show(childFragmentManager, null)
                            viewModel.onIntent(UpdateGpsStatus(it))
                        }
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
                            viewModel.onIntent(UpdateNetworkStatus(it))
                        }

                        NetworkStatus.LOST, NetworkStatus.UNAVAILABLE -> {
                            viewModel.onIntent(UpdateNetworkStatus(NetworkStatus.UNAVAILABLE))
                        }
                    }
                }
            }
        }
    }

}