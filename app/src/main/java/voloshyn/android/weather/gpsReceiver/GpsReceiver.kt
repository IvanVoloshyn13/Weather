package voloshyn.android.weather.gpsReceiver

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface GpsReceiver {
    fun registerLifecycleOwner(context: Context, owner: LifecycleOwner)
  val gpsStatus: Flow<GpsStatus?>

}

fun Boolean.toGpsStatus(): GpsStatus {
    return when (this) {
        true -> {
            GpsStatus.AVAILABLE
        }
        false -> {
            GpsStatus.UNAVAILABLE
        }
    }
}

enum class GpsStatus {
    AVAILABLE, UNAVAILABLE
}

