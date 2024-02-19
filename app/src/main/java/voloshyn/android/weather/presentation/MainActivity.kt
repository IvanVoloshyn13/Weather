package voloshyn.android.weather.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import voloshyn.android.weather.R
import voloshyn.android.weather.gpsReceiver.GpsReceiver
import voloshyn.android.weather.gpsReceiver.GpsReceiverImpl
import voloshyn.android.weather.presentation.dialog.GpsPermissionDialog


@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main), GpsReceiver by GpsReceiverImpl() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerLifecycleOwner(this, this, savedInstanceState)
        checkLocationPermission()

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for (permission in permissions) {
            if (requestCode == LOCATION_REQUEST_CODE) {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    return
                } else if (shouldShowRequestPermissionRationale(permission)) {
                    GpsPermissionDialog().show(supportFragmentManager, null)
                } else {
                    return
                }
            }
        }
    }

    private fun checkLocationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                return
            }

            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                GpsPermissionDialog().show(supportFragmentManager, null)
            }

            else -> {
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_REQUEST_CODE
                )
            }
        }
    }

    companion object {
        const val LOCATION_REQUEST_CODE = 200
    }
}


