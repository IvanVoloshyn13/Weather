package voloshyn.android.weather.fragment.onBoarding.second

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import voloshyn.android.data.popularPlacesStorage.PopularPlace
import voloshyn.android.weather.R
import voloshyn.android.weather.databinding.FragmentOnboardingSecondBinding
import voloshyn.android.weather.databinding.ItemPopularPlacesBinding
import voloshyn.android.weather.fragment.viewBinding

@AndroidEntryPoint
class OnBoardingFragmentSecond : Fragment(R.layout.fragment_onboarding_second),
    OnItemClick {
    private val viewModel by viewModels<OnBoardingViewModel>()
    private val binding by viewBinding<FragmentOnboardingSecondBinding>()
    private val itemBinding by viewBinding<ItemPopularPlacesBinding>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rv = binding.rvPopularPlaces
        val adapter = FavouritePlacesAdapter(this)
        rv.adapter = adapter
        lifecycleScope.launch {
            viewModel.popularPlace.collectLatest {
                adapter.submitList(it)
            }
        }
        binding.bttFinish.setOnClickListener {
            val savedPlacesArray = viewModel.checkedItems()
           findNavController().navigate(R.id.action_secondOnBoardingFragment_to_weatherFragment)
        }
    }


    override fun onItemClick(item: PopularPlace) {
        viewModel.toggleSelection(item)
    }


}