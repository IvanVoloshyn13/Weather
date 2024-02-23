package voloshyn.android.weather.gpsReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.flow.MutableStateFlow

class GpsReceiverImpl : GpsReceiver, LifecycleEventObserver {
    private lateinit var locationManager: LocationManager
    private var context: Context? = null

    override fun registerLifecycleOwner(
        context: Context,
        owner: LifecycleOwner
    ) {
        owner.lifecycle.addObserver(this)
        this.context = context

    }

    override val gpsStatus = MutableStateFlow(true.toGpsStatus())

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            gpsStatus.tryEmit(isGpsEnabled.toGpsStatus())
        }
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                registerReceiver()
                gpsStatus.tryEmit(checkGpsStatus().toGpsStatus())

            }

            Lifecycle.Event.ON_STOP -> {
                context?.unregisterReceiver(receiver)
            }

            Lifecycle.Event.ON_DESTROY -> {
                context = null
            }

            else -> Unit
        }
    }

    private fun checkGpsStatus(): Boolean {
        locationManager =
            context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled: Boolean =
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        return isGpsEnabled
    }

    private fun registerReceiver() {
        context?.registerReceiver(
            receiver,
            IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
        )
    }
}