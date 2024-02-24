package voloshyn.android.weather.networkObserver

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import voloshyn.android.domain.model.NetworkStatus

class NetworkObserverImpl : NetworkObserver, LifecycleEventObserver {
    private var context: Context? = null
    private var connectivityManager: ConnectivityManager? = null
    private var networkJob: Job? = null


    override fun registerNetworkLifecycleOwner(
        context: Context,
        owner: LifecycleOwner
    ) {
        owner.lifecycle.addObserver(this)
        this.context = context

    }

    override val networkStatusFlow: MutableStateFlow<NetworkStatus?> = MutableStateFlow(null)


    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                networkJob = Job()
                connectivityManager =
                    context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                source.lifecycleScope.launch(networkJob as CompletableJob) {
                    observe().collectLatest {
                        networkStatusFlow.emit(it)
                    }
                }
            }

            Lifecycle.Event.ON_STOP -> {
                connectivityManager = null
                networkJob?.cancel()
            }

            Lifecycle.Event.ON_DESTROY -> {
                this.context = null
                networkJob = null

            }

            else -> Unit
        }
    }

    private fun observe(): Flow<NetworkStatus> {
        val networkStatus =
            connectivityManager?.getNetworkCapabilities(connectivityManager?.activeNetwork)
        return callbackFlow<NetworkStatus> {
            launch {
                if (networkStatus == null) {
                    trySend(NetworkStatus.LOST)
                }
            }
            val callback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    launch {
                        trySend(NetworkStatus.AVAILABLE)
                    }
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    launch {
                        trySend(NetworkStatus.LOST)
                    }
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    launch {
                        trySend(NetworkStatus.UNAVAILABLE)
                    }
                }
            }
            connectivityManager?.registerDefaultNetworkCallback(callback)
            awaitClose {
                connectivityManager?.unregisterNetworkCallback(callback)
            }

        }.distinctUntilChanged()
    }


}
