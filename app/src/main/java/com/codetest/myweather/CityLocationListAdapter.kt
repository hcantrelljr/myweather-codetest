package com.codetest.myweather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codetest.myweather.CityLocationListAdapter.CityLocationViewHolder
import com.codetest.myweather.room.CityLocation

class CityLocationListAdapter : ListAdapter<CityLocation, CityLocationViewHolder>(DiffCallback) {

    private lateinit var listener: RecyclerClickListener
    fun setItemListener(listener: RecyclerClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityLocationViewHolder {
        return CityLocationViewHolder.create(parent, listener)
    }

    override fun onBindViewHolder(holder: CityLocationViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.city_location)
    }

    class CityLocationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cityLocationText: TextView = itemView.findViewById(R.id.textView)
        private val itemDelete = itemView.findViewById<ImageView>(R.id.item_delete)
        private val card = itemView.findViewById<CardView>(R.id.card)

        fun bind(text: String?) {
            cityLocationText.text = text
        }

        companion object {
            fun create(parent: ViewGroup, listener: RecyclerClickListener): CityLocationViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.city_location_item, parent, false)

                val holder = CityLocationViewHolder(view)

                holder.itemDelete.setOnClickListener {
                    listener.onItemRemoveClick(holder.adapterPosition)
                }

                holder.card.setOnClickListener {
                    listener.onItemClick(holder.adapterPosition)
                }

                return holder
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<CityLocation>() {
            override fun areItemsTheSame(oldItem: CityLocation, newItem: CityLocation): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: CityLocation, newItem: CityLocation): Boolean {
                return oldItem.city_location == newItem.city_location
            }
        }
    }
}
