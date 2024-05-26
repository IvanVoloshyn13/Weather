package voloshyn.android.weather.presentation.fragment.weather

import android.graphics.Rect
import android.os.Bundle
import android.view.Gravity
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
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import voloshyn.android.data.model.WeatherTypeModel
import voloshyn.android.domain.model.place.Place
import voloshyn.android.domain.model.place.PlacesSizeState
import voloshyn.android.domain.model.weather.components.CurrentForecast
import voloshyn.android.weather.R
import voloshyn.android.weather.databinding.FragmentWeatherBinding
import voloshyn.android.weather.databinding.HeaderLayoutBinding
import voloshyn.android.weather.databinding.ProgressBarBinding
import voloshyn.android.weather.databinding.WidgetForecastBinding
import voloshyn.android.weather.gpsReceiver.GpsReceiver
import voloshyn.android.weather.networkObserver.NetworkObserver
import voloshyn.android.weather.presentation.fragment.viewBinding
import voloshyn.android.weather.presentation.fragment.weather.adapter.DailyAdapter
import voloshyn.android.weather.presentation.fragment.weather.adapter.HourlyAdapter
import voloshyn.android.weather.presentation.fragment.weather.adapter.OnPlaceClickListener
import voloshyn.android.weather.presentation.fragment.weather.adapter.SavedPlacesAdapter
import voloshyn.android.weather.presentation.fragment.weather.mvi.FetchWeatherForCurrentLocation
import voloshyn.android.weather.presentation.fragment.weather.mvi.FetchWeatherForSavedPlace
import voloshyn.android.weather.presentation.fragment.weather.mvi.FetchWeatherForSavedPlaceById
import voloshyn.android.weather.presentation.fragment.weather.mvi.SetCurrentLocationIsActive
import voloshyn.android.weather.presentation.fragment.weather.mvi.SetGpsStatus
import voloshyn.android.weather.presentation.fragment.weather.mvi.TogglePlaces
import voloshyn.android.weather.presentation.fragment.weather.mvi.UpdateNetworkStatus
import voloshyn.android.weather.presentation.fragment.weather.mvi.WeatherState
import javax.inject.Inject

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
    private lateinit var scope: LifecycleCoroutineScope
    private var gpsUnavailableDialog: GpsUnavailableDialog? = null

    @Inject
    lateinit var blurUtil: BlurUtil

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val header = binding.mainNavView.getHeaderView(0)
        scope = viewLifecycleOwner.lifecycleScope
        widgetForecastBinding = WidgetForecastBinding.bind(binding.root)
        progressBarBinding = ProgressBarBinding.bind(binding.root)
        drawerLayout = binding.mainDrawer
        headerBinding = HeaderLayoutBinding.bind(header)
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
        observeGpsStatus()
        observeNetworkStatus()
        initRecyclers()
        setupViewListeners()
        collectUi()
        sideEffects()
        collectTime()

        setFragmentResultListener(CITY_ID_REQUEST_KEY) { _, bundle ->
            val placeID = bundle.getInt(CITY_ID_BUNDLE_KEY)
            if (placeID != null) {
                viewModel.onIntent(FetchWeatherForSavedPlaceById(placeID))
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

    private suspend fun updateImage(url: String) {
        if (url.isNotEmpty()) {
            blurUtil.initialize(binding.backgroundImage.context, url)
            scope.launch {
                viewModel.blurState.collectLatest { blur ->
                    blurUtil.setBlurredImageFromUrl(
                        binding.backgroundImage,
                        (blur.toFloat()) / 8f
                    )
                }
            }
        } else defaultBackground()
    }


    private fun updateHeader(state: WeatherState) {
        savedPlacesAdapter.submitList(state.places.second)
        if (state.places.first <= 4) {
            headerBinding.bttTogglePlacesSize.visibility = View.GONE
        } else {
            headerBinding.bttTogglePlacesSize.visibility = View.VISIBLE
        }
        when (state.placesState) {
            PlacesSizeState.FULL -> {
                headerBinding.tvToggleText.text = getString(R.string.show_less)
                headerBinding.icToggleIcon.setImageResource(R.drawable.ic_expand_less)
            }

            PlacesSizeState.TRIM -> {
                headerBinding.tvToggleText.text = getString(R.string.show_more)
                headerBinding.icToggleIcon.setImageResource(R.drawable.ic_expand_more)
            }

            PlacesSizeState.DEFAULT -> {
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
            viewModel.onIntent(SetCurrentLocationIsActive(true))
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

    private fun updateMainWeatherWidget(state: CurrentForecast) {
        binding.apply {
            tvCurrentTemp.text = state.currentTemperature.toString()
            tvMaxTemp.text = state.maxTemperature.toString()
            tvMinTemp.text = state.minTemperature.toString()
            tvWeatherTypeDesc.text = WeatherTypeModel.fromWHO(state.weatherCode).weatherType
            ivWeatherTypeIcon.setImageResource(WeatherTypeModel.fromWHO(state.weatherCode).weatherIcon)
        }
    }

    private fun initRecyclers() {
        hourlyAdapter = HourlyAdapter()
        dailyAdapter = DailyAdapter()
        savedPlacesAdapter = SavedPlacesAdapter(this)
        //Hourly
        widgetForecastBinding.rvHourlyForecast.adapter = hourlyAdapter
        //Daily
        widgetForecastBinding.rvDaily.adapter = dailyAdapter
        //Places
        headerBinding.rvPlaces.adapter = savedPlacesAdapter
    }

    private fun sideEffects() {
        scope.launch {
            viewModel.uiEffects.collectLatest {
                if (it.isError) {
                    val messageFromResources = getString(it.message.stringRes)
                    val message = it.message.message
                    Toast.makeText(
                        requireContext(),
                        messageFromResources,
                        Toast.LENGTH_LONG
                    ).show()
                    if (message.isNotBlank()) {
                        Toast.makeText(
                            requireContext(),
                            message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    private fun collectTime() {
        scope.launch {
            viewModel.time.collectLatest {
                binding.toolbar.tvCurrentTime.text = it
            }
        }
    }

    private fun observeGpsStatus() {
        val fragmentAct = requireActivity() as GpsReceiver
        scope.launch {
            fragmentAct.gpsStatus.collectLatest { status ->
                status?.let {
                    viewModel.onIntent(SetGpsStatus(it))
                }
            }
        }
    }

    private fun observeNetworkStatus() {
        val fragmentAct = requireActivity() as NetworkObserver
        scope.launch {
            fragmentAct.networkStatusFlow.collectLatest { network ->
                network?.let {
                    viewModel.onIntent(UpdateNetworkStatus(it))
                }
            }
        }
    }

    override fun onItemClick(place: Place) {
        drawerLayout.close()
        viewModel.onIntent(FetchWeatherForSavedPlace(place))
        viewModel.onIntent(SetCurrentLocationIsActive(false))
    }

    private fun collectUi() {
        scope.launch {
            viewModel.state.collectLatest { state ->
                renderStateToUi(
                    state.isLoading,
                    state.isError,
                    state.errorMessage
                ) {
                    updateMainWeatherWidget(state.currentForecast)
                    updateWidgetForecast(state)
                    updateHeader(state)
                    updateImage(state.imageUrl)
                    binding.toolbar.tvToolbarTitle.text = state.placeName
                    if (state.showGpsDisabledDialog) {
                        gpsUnavailableDialog = GpsUnavailableDialog()
                        gpsUnavailableDialog?.show(childFragmentManager, null)
                    } else {
                        gpsUnavailableDialog?.dialog?.dismiss()
                        gpsUnavailableDialog = null
                    }
                }

            }

        }

    }

    private suspend fun renderStateToUi(
        isLoading: Boolean, isError: Boolean, errorMessage: String,
        onSuccess: suspend () -> Unit
    ) {
        binding.scroll.visibility = if (isLoading || isError) View.INVISIBLE else View.VISIBLE
        progressBarBinding.progressBar.visibility =
            if (isLoading) View.VISIBLE else View.GONE

        binding.errorDialog.errorDialog.visibility =
            if (!isError) View.GONE else View.VISIBLE
        when {
            isLoading -> {
                defaultBackground()
            }

            isError -> {
                defaultBackground()
                binding.errorDialog.tvError.text = errorMessage
            }

            else -> {
                onSuccess()
            }
        }

    }

    private fun defaultBackground() {
        binding.backgroundImage.setImageDrawable(
            AppCompatResources.getDrawable(
                requireContext(),
                R.drawable.splash
            )
        )
        binding.backgroundImage.scaleType = ImageView.ScaleType.FIT_XY
    }

    companion object {
        const val CITY_ID_REQUEST_KEY = "city_id_request_key"
        const val CITY_ID_BUNDLE_KEY = "city_id_bundle_key"
    }

}
