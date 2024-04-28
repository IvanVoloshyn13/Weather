package voloshyn.android.domain

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