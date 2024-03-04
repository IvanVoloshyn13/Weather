package voloshyn.android.domain.useCase

import voloshyn.android.domain.Resource

fun <T> Resource<T>.toResult(
): T {
    return when (this) {
        is Resource.Success -> {
            data
        }

        is Resource.Error -> {
            throw e
        }
    }
}