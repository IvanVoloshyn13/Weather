package voloshyn.android.weather.presentation.fragment.onBoarding.second

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import voloshyn.android.data.dataSource.popularPlacesStorage.PopularPlaceData
import voloshyn.android.weather.R
import voloshyn.android.weather.databinding.FragmentOnboardingSecondBinding
import voloshyn.android.weather.presentation.fragment.viewBinding

@AndroidEntryPoint
class OnBoardingFragmentSecond : Fragment(R.layout.fragment_onboarding_second),
    OnItemClick {
    private val viewModel by viewModels<OnBoardingViewModel>()
    private val binding by viewBinding<FragmentOnboardingSecondBinding>()
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
            viewModel.save(viewModel.checkedItems())

            val directions =
                OnBoardingFragmentSecondDirections.actionSecondOnBoardingFragmentToWeatherFragment()
            findNavController().navigate(directions,
                navOptions {
                    anim {
                        enter = R.anim.enter
                        exit = R.anim.exit
                    }
                    popUpTo(R.id.secondOnBoardingFragment) {
                        inclusive = true
                    }
                }
            )

        }
    }


    override fun onItemClick(item: PopularPlaceData) {
        viewModel.toggleSelection(item)
    }


}