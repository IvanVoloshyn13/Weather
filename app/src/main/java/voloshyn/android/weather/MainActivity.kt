package voloshyn.android.weather

import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {


    override fun onResume() {
        super.onResume()
    }

}

data class City(val id: Int, val name: String, val isFavourite: Boolean)

fun main() {
    val cities = listOf(City(1, "New York", false), City(2, "London", false), City(3, "Paris", false))
    val favoriteCities = listOf(City(1, "New York", true), City(2, "London", true))

    val combinedList = (favoriteCities + cities.filterNot { it.id in favoriteCities.map { favCity -> favCity.id } })
        .distinctBy { it.id }

    combinedList.forEach { println(it) }
}
