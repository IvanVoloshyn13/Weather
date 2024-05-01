package voloshyn.android.weather.presentation.fragment.weather.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import voloshyn.android.domain.model.Place
import voloshyn.android.weather.R
import voloshyn.android.weather.databinding.ItemPlacesBinding

class SavedPlacesAdapter(private val listener: OnPlaceClickListener) :
    RecyclerView.Adapter<SavedPlacesAdapter.CitiesItemViewHolder>(), View.OnClickListener {
    private var places = ArrayList<Place>()

    inner class CitiesItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemPlacesBinding.bind(itemView)
        fun bind(place: Place) {
            binding.apply {
                tvName.text = place.name
                ivIcon.setImageResource(R.drawable.ic_current_location)
            }
        }

        init {
            itemView.setOnClickListener(this@SavedPlacesAdapter)
        }
    }

    override fun onClick(v: View) {
        val place = v.tag as Place
        listener.onClick(place)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitiesItemViewHolder {
        return CitiesItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_places, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return places.size
    }

    override fun onBindViewHolder(holder: CitiesItemViewHolder, position: Int) {
        val place = places[position]
        holder.bind(place)
        holder.itemView.tag = place
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<Place>) {
        places.clear()
        places.addAll(list)
        notifyDataSetChanged() //TODO() setup this method with more specific change event if it have sense
    }

}

interface OnPlaceClickListener {
    fun onClick(place: Place)
}