package voloshyn.android.weather.presentation.fragment.addSearchPlaces

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import voloshyn.android.domain.model.addSearchPlace.SearchPlace
import voloshyn.android.weather.R
import voloshyn.android.weather.databinding.ItemSearchPlaceBinding

class SearchedPlacesAdapter(private val listener: RecyclerViewOnItemClick) :
    ListAdapter<SearchPlace, SearchedPlacesAdapter.CitiesViewHolder>(CitiesDiffUtil()) {

    private val citiesList: ArrayList<SearchPlace> = ArrayList<SearchPlace>()

    inner class CitiesViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        init {
            view.setOnClickListener(this)
        }

        private val binding = ItemSearchPlaceBinding.bind(view)
        fun bind(item: SearchPlace) {
            binding.tvName.text = item.toString()
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(citiesList[position])
            }
        }
    }

    class CitiesDiffUtil() : DiffUtil.ItemCallback<SearchPlace>() {
        override fun areItemsTheSame(oldItem: SearchPlace, newItem: SearchPlace): Boolean {
            return oldItem.latitude == newItem.latitude
        }

        override fun areContentsTheSame(oldItem: SearchPlace, newItem: SearchPlace): Boolean {
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

    fun submitList1(list: List<SearchPlace>) {
        citiesList.clear()
        citiesList.addAll(list)
        notifyDataSetChanged() //TODO() setup this method with more specific change event
    }

    interface RecyclerViewOnItemClick {
        fun onItemClick(city: SearchPlace)
    }
}