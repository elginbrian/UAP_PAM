package com.elginbrian.uappam.presentation.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.elginbrian.uappam.R
import com.elginbrian.uappam.data.model.Plant

class PlantAdapter(
    private val onDetailClick: (Plant) -> Unit,
    private val onDeleteClick: (Plant) -> Unit
) : ListAdapter<Plant, PlantAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_plant, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val plant = getItem(position)
        if (plant != null) {
            holder.bind(plant)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val plantName: TextView = itemView.findViewById(R.id.tv_plant_name)
        private val plantPrice: TextView = itemView.findViewById(R.id.tv_plant_price)
        private val plantPhoto: ImageView = itemView.findViewById(R.id.iv_plant_photo)
        private val deleteButton: Button = itemView.findViewById(R.id.btn_delete)
        private val detailButton: Button = itemView.findViewById(R.id.btn_detail)

        fun bind(plant: Plant) {
            plantName.text = plant.plantName
            plantPrice.text = plant.price
            plantPhoto.setImageResource(R.drawable.plant_image)

            detailButton.setOnClickListener { onDetailClick(plant) }
            deleteButton.setOnClickListener { onDeleteClick(plant) }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Plant>() {
            override fun areItemsTheSame(oldItem: Plant, newItem: Plant): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Plant, newItem: Plant): Boolean {
                return oldItem == newItem
            }
        }
    }
}