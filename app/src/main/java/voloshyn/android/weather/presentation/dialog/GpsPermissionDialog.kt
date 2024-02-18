package voloshyn.android.weather.presentation.dialog

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import voloshyn.android.weather.presentation.MainActivity
import voloshyn.android.weather.R
import voloshyn.android.weather.databinding.DialogLocationPermissionBinding
import voloshyn.android.weather.presentation.fragment.viewBinding

class GpsPermissionDialog : DialogFragment(R.layout.dialog_location_permission) {
    private val binding: DialogLocationPermissionBinding by viewBinding<DialogLocationPermissionBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bttEnableLocation.setOnClickListener {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                MainActivity.LOCATION_REQUEST_CODE
            )
           requireDialog().cancel()

        }
        binding.bttLater.setOnClickListener {
            requireDialog().cancel()
        }
    }
}