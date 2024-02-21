package voloshyn.android.weather.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import voloshyn.android.domain.Resource
import voloshyn.android.domain.useCase.mainActivity.GetOnBoardingStatusUseCase
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val onBoarding: GetOnBoardingStatusUseCase
):ViewModel() {


    private val _onBoardingStatus = MutableSharedFlow<OnBoardingStatus>()

    val onBoardingStatus = _onBoardingStatus.asSharedFlow()

    init {
        viewModelScope.launch {
            isOnBoardingCompleted()
        }
    }

    private suspend fun isOnBoardingCompleted() {
        val completed = onBoarding.invoke()
        when (completed) {
            is Resource.Success -> {
                completed.data.let { completed ->
                    _onBoardingStatus.emit(OnBoardingStatus(completed))
                }
            }
            is Resource.Error -> {}
            is Resource.Loading -> {}
        }
    }




}

data class OnBoardingStatus(
    val completed: Boolean
)
