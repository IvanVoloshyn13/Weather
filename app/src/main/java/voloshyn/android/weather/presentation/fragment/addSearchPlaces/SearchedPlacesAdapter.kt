package voloshyn.android.weather.presentation.fragment.addSearchPlaces

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import voloshyn.android.domain.model.place.Place
import voloshyn.android.weather.R
import voloshyn.android.weather.databinding.ItemSearchPlaceBinding

class SearchedPlacesAdapter(private val listener: RecyclerViewOnItemClick) :
    ListAdapter<Place, SearchedPlacesAdapter.CitiesViewHolder>(CitiesDiffUtil()) {

    private val citiesList: ArrayList<Place> = ArrayList<Place>()

    inner class CitiesViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        init {
            view.setOnClickListener(this)
        }

        private val binding = ItemSearchPlaceBinding.bind(view)
        fun bind(item: Place) {
            binding.tvName.text = item.toString()
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(citiesList[position])
            }
        }
    }

    class CitiesDiffUtil() : DiffUtil.ItemCallback<Place>() {
        override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean {
            return oldItem.latitude == newItem.latitude
        }

        override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean {
            return oldItem.name == newItem.name && oldItem.latitude == newItem.latitude
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitiesViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_search_place, parent, false)
        return CitiesViewHolder(view)
    }

    override fun onBindViewHolder(holder: CitiesViewHolder, position: Int) {
        val item = citiesList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return citiesList.size
    }

    fun submitList1(list: List<Place>) {
        citiesList.clear()
        citiesList.addAll(list)
        notifyDataSetChanged() //TODO() setup this method with more specific change event
    }

    interface RecyclerViewOnItemClick {
        fun onItemClick(place: Place)
    }
}