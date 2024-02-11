package voloshyn.android.weather

import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import voloshyn.android.weather.fragment.onBoarding.FirstOnBoardingFragment

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onResume() {
        super.onResume()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, FirstOnBoardingFragment()).commit()
    }

}
