package voloshyn.android.weather.presentation.fragment.weather

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.fragment.app.DialogFragment
import voloshyn.android.weather.R
import voloshyn.android.weather.databinding.DialogNoGpsBinding
import voloshyn.android.weather.presentation.fragment.viewBinding

class GpsUnavailableDialog : DialogFragment(R.layout.dialog_no_gps) {
    private val binding: DialogNoGpsBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bttEnabledLocation.setOnClickListener {
            showAppSettings()
        }
    }

    private fun showAppSettings() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }
}