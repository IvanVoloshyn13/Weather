package voloshyn.android.domain

sealed class Resource<out T> {
    class Error<T>(val e:Exception) : Resource<T>()
    class Success<T>(val data: T) : Resource<T>()
}
