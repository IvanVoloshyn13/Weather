package voloshyn.android.domain

sealed class Resource<out T> {
    class Loading<T> : Resource<T>()
    class Error<T>(message: String?) : Resource<T>()
    class Success<T>(data: T) : Resource<T>()
}
