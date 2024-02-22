package voloshyn.android.domain

sealed class Resource<out T> {
    class Error<T>(val message: String?) : Resource<T>()
    class Success<T>(val data: T) : Resource<T>()
}
