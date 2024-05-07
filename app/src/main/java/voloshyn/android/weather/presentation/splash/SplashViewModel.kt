package voloshyn.android.weather.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import voloshyn.android.domain.appError.AppResult
import voloshyn.android.domain.useCase.mainActivity.GetOnBoardingStatusUseCase
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val onBoarding: GetOnBoardingStatusUseCase
) : ViewModel() {


    private val _onBoardingStatus = MutableSharedFlow<OnBoardingStatus>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val onBoardingStatus = _onBoardingStatus.asSharedFlow()

    init {
        viewModelScope.launch {
            isOnBoardingCompleted()
        }
    }

    private suspend fun isOnBoardingCompleted() {

        val completed = onBoarding.invoke()
        when (completed) {
            is AppResult.Success -> {
                completed.data.let { completed ->
                    _onBoardingStatus.emit(
                        OnBoardingStatus(
                            completed = completed,
                            isLoading = false
                        )
                    )
                }
            }

            is AppResult.Error -> {
                completed.error.let { exception ->
                    _onBoardingStatus.emit(
                        OnBoardingStatus(
                            isError = true,
                            completed = false,
                            isLoading = false,
                            errorMessage = TODO()
                        )
                    )
                }
            }

            else -> {}
        }
    }


}

data class OnBoardingStatus(
    val completed: Boolean,
    val isError: Boolean = false,
    val isLoading: Boolean = true,
    val errorMessage: String? = ""
)
