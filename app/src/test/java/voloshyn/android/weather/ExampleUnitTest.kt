package voloshyn.android.weather

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}


data class PushNotifications(
    val notifyEverydayWeather: Boolean,
    val notifyUpcomingRainfall: Boolean,
    val notifyTemperatureChanges: Boolean,
    val notifyWeatherAlert: Boolean
) {

    val showNotifications: Boolean
        get() = (notifyEverydayWeather || notifyUpcomingRainfall || notifyTemperatureChanges || notifyWeatherAlert)
}


class PushNotificationsTest {
    private val notifyEverydayWeather = true
    private val notifyUpcomingRainfall = false
    private val notifyWeatherAlert = true
    private val notifyTemperatureChanges = false

@Test
    fun showNotification() {
        // Given
        val pushNotifications = PushNotifications(
            notifyEverydayWeather,
            notifyUpcomingRainfall,
            notifyTemperatureChanges,
            notifyWeatherAlert
        )

        val showNotifications = pushNotifications.showNotifications
        println(showNotifications)


    }
}