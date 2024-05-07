package voloshyn.android.weather.presentation.splash

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import voloshyn.android.weather.R
import voloshyn.android.weather.databinding.FragmentSplashBinding
import voloshyn.android.weather.presentation.MainActivity
import voloshyn.android.weather.presentation.MainActivityArgs
import voloshyn.android.weather.presentation.fragment.base.renderSimpleResult
import voloshyn.android.weather.presentation.fragment.base.viewBinding

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {
    private val viewModel: SplashViewModel by viewModels()
    private val binding: FragmentSplashBinding by viewBinding()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderAnimations()
        lifecycleScope.launch {
                viewModel.onBoardingStatus.collectLatest {
                    renderSimpleResult(
                        binding.root,
                        isError = it.isError,
                        isLoading = it.isLoading,
                        onError = {
                            binding.errorDialog.errorDialog.visibility = View.VISIBLE
                        },
                        onLoading = {
                            Unit
                        }) {
                        launchMainScreen(it.completed)
                    }
                }
            }
    }

    private fun launchMainScreen(completed: Boolean) {
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        val args = MainActivityArgs(completed)
        intent.putExtras(args.toBundle())
        startActivity(intent)

    }



    private fun renderAnimations() {
        binding.loadingIndicator.alpha = 0f
        binding.loadingIndicator.animate()
            .alpha(0.7f)
            .setDuration(1000)
            .start()

        binding.pleaseWaitTextView.alpha = 0f
        binding.pleaseWaitTextView.animate()
            .alpha(1f)
            .setStartDelay(500)
            .setDuration(1000)
            .start()
    }


}

