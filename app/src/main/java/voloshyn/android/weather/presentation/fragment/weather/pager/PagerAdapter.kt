package voloshyn.android.weather.presentation.fragment.weather.pager

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import voloshyn.android.domain.model.weather.WeatherComponents

class PagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    private val list = ArrayList<WeatherComponents>()
    override fun getItemCount(): Int {
        return list.size + 1
    }

    override fun createFragment(position: Int): Fragment {
        TODO()
    }

    fun submitList(newList: ArrayList<WeatherComponents>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }
}