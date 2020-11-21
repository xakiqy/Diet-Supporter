package com.example.diet_supporter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.diet_supporter.database.PersonalFood
import com.example.diet_supporter.databinding.PersonalFoodListBinding

class PersonalFoodAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<PersonalFood, PersonalFoodAdapter.PersonalFoodDataViewHolder>(
        DiffCallback
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PersonalFoodAdapter.PersonalFoodDataViewHolder {
        return PersonalFoodDataViewHolder(
            PersonalFoodListBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(
        holder: PersonalFoodAdapter.PersonalFoodDataViewHolder,
        position: Int
    ) {
        val foodProperty = getItem(position)
        holder.bind(foodProperty, onClickListener)

    }

    companion object DiffCallback : DiffUtil.ItemCallback<PersonalFood>() {
        override fun areItemsTheSame(
            oldItem: PersonalFood,
            newItem: PersonalFood
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: PersonalFood,
            newItem: PersonalFood
        ): Boolean {
            return oldItem.food_id == newItem.food_id
        }


    }

    class PersonalFoodDataViewHolder(private var binding: PersonalFoodListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(foodProperty: PersonalFood, onClickListener: OnClickListener) {
            binding.property = foodProperty

            itemView.setOnClickListener { onClickListener.onClick(foodProperty, binding.imageView) }

            binding.executePendingBindings()
        }
    }

    class OnClickListener(val clickListener: (foodProperty: PersonalFood, ImageView) -> Unit) {
        fun onClick(foodProperty: PersonalFood, imageView: ImageView) =
            clickListener(foodProperty, imageView)
    }
}