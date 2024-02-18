package voloshyn.android.weather.presentation.fragment.onBoarding.second

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import voloshyn.android.data.popularPlacesStorage.PopularPlaceData
import voloshyn.android.weather.R
import voloshyn.android.weather.databinding.ItemPopularPlacesBinding

class FavouritePlacesAdapter(
    private val listener: OnItemClick,
) : RecyclerView.Adapter<FavouritePlacesAdapter.PopularPlaceViewHolder>() {

    private val list: ArrayList<PopularPlaceData> = ArrayList()

    inner class PopularPlaceViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
        }

        private val binding = ItemPopularPlacesBinding.bind(itemView)
        fun bind(data: PopularPlaceData) {
            with(binding) {
                ivPlace.setImageResource(data.image)
                placesName.text = itemView.context.getString(data.name)
                placesName.setTextColor(
                    if (data.isChecked) itemView.resources.getColor(R.color.light_white, null)
                    else itemView.resources.getColor(R.color.light_gray, null)
                )
                checkboxPopularPlaces.isChecked = data.isChecked
                checkboxPopularPlaces.isVisible = data.isChecked
            }
        }

        override fun onClick(v: View?) {
            listener.onItemClick(itemView.tag as PopularPlaceData)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularPlaceViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_popular_places, parent, false)
        return PopularPlaceViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: PopularPlaceViewHolder, position: Int) {
        val item = list[position]
        holder.itemView.tag = item
        holder.bind(list[position])

    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(items: List<PopularPlaceData>) {
        val position = list.zip(items).indexOfFirst { (a, b) -> a != b }
        list.clear()
        list.addAll(items)
        if (position != -1) {
            notifyItemChanged(position)
        } else {
            notifyDataSetChanged()
        }
    }
}

interface OnItemClick {
    fun onItemClick(item: PopularPlaceData)
}

