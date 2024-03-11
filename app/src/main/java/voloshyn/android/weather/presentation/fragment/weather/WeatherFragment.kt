package voloshyn.android.weather.presentation.fragment.weather

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import voloshyn.android.data.repository.weather.WeatherTypeRepository
import voloshyn.android.domain.model.NetworkStatus
import voloshyn.android.domain.model.addSearchPlace.ListSizeState
import voloshyn.android.domain.model.addSearchPlace.Place
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
import voloshyn.android.weather.presentation.fragment.weather.adapter.OnPlaceClickListener
import voloshyn.android.weather.presentation.fragment.weather.adapter.SavedPlacesAdapter
import voloshyn.android.weather.presentation.fragment.weather.mvi.FetchWeatherForCurrentLocation
import voloshyn.android.weather.presentation.fragment.weather.mvi.FetchWeatherForSavedPlace
import voloshyn.android.weather.presentation.fragment.weather.mvi.TogglePlaces
import voloshyn.android.weather.presentation.fragment.weather.mvi.UpdateGpsStatus
import voloshyn.android.weather.presentation.fragment.weather.mvi.UpdateNetworkStatus
import voloshyn.android.weather.presentation.fragment.weather.mvi.WeatherState


@AndroidEntryPoint
class WeatherFragment : Fragment(R.layout.fragment_weather), OnPlaceClickListener {
    private val binding by viewBinding<FragmentWeatherBinding>()
    private lateinit var widgetForecastBinding: WidgetForecastBinding
    private lateinit var headerBinding: HeaderLayoutBinding
    private lateinit var progressBarBinding: ProgressBarBinding
    private lateinit var drawerLayout: DrawerLayout
    private val viewModel: WeatherViewModel by viewModels()
    private lateinit var hourlyAdapter: HourlyAdapter
    private lateinit var dailyAdapter: DailyAdapter
    private lateinit var savedPlacesAdapter: SavedPlacesAdapter


    override fun onAttach(context: Context) {
        super.onAttach(context)
        observeGpsStatus()
        observeNetworkStatus()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        widgetForecastBinding = WidgetForecastBinding.bind(binding.root)
        progressBarBinding = ProgressBarBinding.bind(binding.root)
        val displayMetrics = requireContext().resources.displayMetrics
        val screenHeight = displayMetrics.heightPixels

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBarInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.layoutWeatherType.updateLayoutParams<MarginLayoutParams> {
                topMargin = measuredCurrentWeatherWidgetMargins(
                    screenHeight = screenHeight, systemBarInsets = systemBarInsets
                )
            }
            insets
        }

        calculateVisibilityPercentage()
        drawerLayout = binding.mainDrawer
        val header = binding.mainNavView.getHeaderView(0)
        headerBinding = HeaderLayoutBinding.bind(header)
        hourlyAdapter = HourlyAdapter()
        dailyAdapter = DailyAdapter()
        savedPlacesAdapter = SavedPlacesAdapter(this)
        initDailyRecycler()
        initHourlyRecycler()
        initPlacesRecycler()
        setupViewListeners()
        renderUi()
        sideEffects()
        collectTime()

        setFragmentResultListener("locationId") { _, bundle ->
            val cityId = bundle.getInt("bundle_key")
            if (cityId != null) {
                viewModel.onIntent(FetchWeatherForSavedPlace(cityId))
                viewModel.onIntent(TogglePlaces)
            }
        }
    }

    private fun measuredCurrentWeatherWidgetMargins(
        screenHeight: Int, systemBarInsets: androidx.core.graphics.Insets
    ): Int {
        val insetsValue = systemBarInsets.top + systemBarInsets.bottom
        val layoutMeasure = with(binding) {
            layoutWeatherType.measuredHeight + layoutDailyTemp.measuredHeight + tvCurrentTemp.measuredHeight
        }
        return screenHeight - insetsValue - layoutMeasure
    }

    private fun renderUi() {
        viewLifecycleOwner.lifecycleScope.launch {
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
                        updateUi(state)

                    }, onSuccess = {
                        updateUi(state)
                        binding.errorDialog.errorDialog.visibility = View.GONE
                        progressBarBinding.progressBar.visibility = View.GONE
                    }
                )
            }

        }

    }

    private suspend fun updateUi(state: WeatherState) {
        updateMainWeatherWidget(state.mainWeatherInfo)
        updateWidgetForecast(state)
        updateHeader(state.placesState)
        binding.apply {
            binding.toolbar.tvToolbarTitle.text = state.location
            if (state.imageUrl.isNotEmpty()) {
                //TODO() blurState do not equal to 0 when view is not visible
                BlurUtil.setBlurredImageFromUrl(
                    binding.backgroundImage.context,
                    binding.backgroundImage,
                    state.imageUrl,
                    (state.weatherWidgetVisibility.toFloat()) / 8f
                )
            } else {
                backgroundImage.setImageDrawable(
                    AppCompatResources.getDrawable(
                        requireContext(),
                        R.drawable.splash
                    )
                )
                backgroundImage.scaleType = ImageView.ScaleType.FIT_XY
            }
//TODO() need to add list size to state for updating button visibility

//            if (state.places.size <= 4) {
//                headerBinding.bttTogglePlacesSize.visibility = View.VISIBLE
//            } else {
//                headerBinding.bttTogglePlacesSize.visibility = View.GONE
//            }
            savedPlacesAdapter.submitList(state.places)

        }


    }

    private fun updateHeader(placesState: ListSizeState) {
        when (placesState) {
            ListSizeState.FULL -> {
                headerBinding.tvToggleText.text = getString(R.string.show_less)
                headerBinding.icToggleIcon.setImageResource(R.drawable.ic_expand_less)
            }

            else -> {
                headerBinding.tvToggleText.text = getString(R.string.show_more)
                headerBinding.icToggleIcon.setImageResource(R.drawable.ic_expand_more)
            }
        }

    }

    private fun calculateVisibilityPercentage() {
        var percentage = 0.0
        val calculateView = widgetForecastBinding.widgetForecast
        binding.scroll.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            val scrollBounds = Rect()
            v.getDrawingRect(scrollBounds)
            val topOfView = calculateView.y
            val bottomOfView = topOfView + calculateView.height
            val topOfScrollView = scrollBounds.top
            val bottomOfScrollView = scrollBounds.bottom
            if (topOfScrollView <= topOfView && bottomOfScrollView >= bottomOfView) {
                percentage = 100.0
            } else if ((topOfScrollView >= topOfView && topOfScrollView <= bottomOfView) || (bottomOfScrollView >= topOfView && bottomOfScrollView <= bottomOfView)) {
                // at this point our concerned child is visible
                // now lets get the percentage
                percentage = if (calculateView.height > v.height) {
                    // child height is more than NestedScrollView
                    val percentageCalculation =
                        (((bottomOfView - bottomOfScrollView) / widgetForecastBinding.widgetForecast.height) * 100)
                    if (percentageCalculation > 0) {
                        100 - percentageCalculation.toDouble()
                    } else 100.0
                } else {
                    // child height is more than NestedScrollView
                    (((bottomOfScrollView - topOfView) / calculateView.height) * 100).toDouble()
                }
            }
            viewModel.updateWeatherWidgetVisibility(percentage)
        }
    }

    private fun setupViewListeners() {
        binding.toolbar.mainToolbar.setNavigationOnClickListener {
            binding.mainDrawer.openDrawer(GravityCompat.START)
        }
        headerBinding.currentLocation.cityLayout.setOnClickListener {
            viewModel.onIntent(FetchWeatherForCurrentLocation)
            drawerLayout.close()
        }

        binding.toolbar.bttAddNewCity.setOnClickListener {
            findNavController().navigate(R.id.action_weatherFragment_to_addSearchFragment)
        }

        headerBinding.bttTogglePlacesSize.setOnClickListener {
            viewModel.onIntent(TogglePlaces)
        }
    }

    private fun updateWidgetForecast(state: WeatherState) {
        dailyAdapter.submitList(state.dailyForecast)
        hourlyAdapter.submitList(state.hourlyForecast)
    }

    private fun updateMainWeatherWidget(state: MainWeatherInfo) {
        binding.apply {
            tvCurrentTemp.text = state.currentTemperature.toString()
            tvMaxTemp.text = state.maxTemperature.toString()
            tvMinTemp.text = state.minTemperature.toString()
            tvWeatherTypeDesc.text = WeatherTypeRepository.fromWHO(state.weatherCode).weatherType
            ivWeatherTypeIcon.setImageResource(WeatherTypeRepository.fromWHO(state.weatherCode).weatherIcon)
        }

    }

    private fun initHourlyRecycler() {
        widgetForecastBinding.rvHourlyForecast.adapter = hourlyAdapter
        widgetForecastBinding.rvHourlyForecast.layoutManager = LinearLayoutManager(
            this@WeatherFragment.requireContext(), RecyclerView.HORIZONTAL, false
        )
    }

    private fun initDailyRecycler() {
        widgetForecastBinding.rvDaily.adapter = dailyAdapter
        widgetForecastBinding.rvDaily.layoutManager = LinearLayoutManager(
            this@WeatherFragment.requireContext(), RecyclerView.VERTICAL, false
        )
    }

    private fun initPlacesRecycler() {
        val rv = headerBinding.rvPlaces
        rv.adapter = savedPlacesAdapter
    }

    private fun sideEffects() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.sideEffects.collectLatest {
                if (it.showErrorMessage) {
                    Toast.makeText(requireContext(), it.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun collectTime() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.time.collectLatest {
                binding.toolbar.tvCurrentTime.text = it
            }
        }

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
        lifecycleScope.launch {
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

    override fun onClick(place: Place) {
        drawerLayout.close()
        viewModel.onIntent(FetchWeatherForSavedPlace(place.id))
    }

}