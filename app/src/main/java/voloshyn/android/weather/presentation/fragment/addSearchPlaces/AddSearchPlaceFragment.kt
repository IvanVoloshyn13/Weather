package voloshyn.android.weather.presentation.fragment.addSearchPlaces

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import voloshyn.android.domain.model.place.Place
import voloshyn.android.weather.R
import voloshyn.android.weather.databinding.FragmentPlaceSearchBinding
import voloshyn.android.weather.presentation.fragment.viewBinding
import voloshyn.android.weather.presentation.fragment.weather.WeatherFragment

@AndroidEntryPoint
class AddSearchPlaceFragment : Fragment(R.layout.fragment_place_search),
    SearchedPlacesAdapter.RecyclerViewOnItemClick {
    private val binding by viewBinding<FragmentPlaceSearchBinding>()
    private val viewModel: SearchViewModel by viewModels()
    private var searchJob: Job? = null
    private val searchDelay: Long = 500
    private lateinit var searchedAdapter: SearchedPlacesAdapter
    private lateinit var onBackPressed: OnBackPressedCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onBackPressed = object : OnBackPressedCallback(enabled = true) {
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressed)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchView.apply {
            isActivated = true
            setIconifiedByDefault(false)
            queryHint = getString(R.string.please_enter_city_name)
        }
        searchedAdapter =
            SearchedPlacesAdapter(this as SearchedPlacesAdapter.RecyclerViewOnItemClick)

        val recycler = binding.rvLocation
        recycler.adapter = searchedAdapter
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if (it.length >= 3) {
                        viewModel.search(it)
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchJob?.cancel()
                searchJob = viewLifecycleOwner.lifecycleScope.launch {
                    newText?.let { query ->
                        if (query.length >= 3) {
                            delay(searchDelay)
                            viewModel.search(query)
                        }
                        if (query.isEmpty()) {
                            viewModel.search(query)
                        }
                    }
                }
                return true
            }
        })
        lifecycleScope.launch {
            viewModel.state.collectLatest {
                binding.progressBar.visibility = if (it.isLoading) View.VISIBLE else View.GONE
                searchedAdapter.submitList1(it.places)
            }
        }

        lifecycleScope.launch {
            viewModel.errorState.collectLatest {
                if(it.isError){
                    Toast.makeText(requireContext(), getString(it.errorMessage) , Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun onBackPressed() {
        viewModel.search("")
        findNavController().popBackStack()
    }

    override fun onItemClick(place: Place) {
        viewModel.savePlace(place)
        setFragmentResult(
            WeatherFragment.CITY_ID_REQUEST_KEY,
            bundleOf(WeatherFragment.CITY_ID_BUNDLE_KEY to place.id)
        )
        onBackPressed()
    }


}