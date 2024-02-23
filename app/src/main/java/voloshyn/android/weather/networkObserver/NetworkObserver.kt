package voloshyn.android.weather.networkObserver

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.flow.Flow
import voloshyn.android.domain.model.NetworkStatus


interface NetworkObserver {
    val networkStatusFlow: Flow<NetworkStatus?>

    fun registerNetworkLifecycleOwner(context: Context, owner: LifecycleOwner)
}