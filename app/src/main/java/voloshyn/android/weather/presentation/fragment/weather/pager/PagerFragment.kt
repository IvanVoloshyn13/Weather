package voloshyn.android.weather.presentation.fragment.weather.pager

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import voloshyn.android.weather.R
import voloshyn.android.weather.databinding.FragmentViewPagerBinding
import voloshyn.android.weather.presentation.fragment.viewBinding

@AndroidEntryPoint
class PagerFragment : Fragment(R.layout.fragment_view_pager) {
    private val binding by viewBinding<FragmentViewPagerBinding>()
    private val viewModel: PagerViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


}