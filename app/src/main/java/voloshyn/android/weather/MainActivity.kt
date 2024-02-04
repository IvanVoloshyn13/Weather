package voloshyn.android.weather

import androidx.appcompat.app.AppCompatActivity
import voloshyn.android.weather.fragments.FirstWelcomeFragment

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onResume() {
        super.onResume()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, FirstWelcomeFragment()).commit()
    }

}