package com.xakiqy.diet_supporter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.xakiqy.diet_supporter.database.Food
import com.xakiqy.diet_supporter.databinding.FoodListViewBinding

class FoodAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<Food, FoodAdapter.FoodDataViewHolder>(
        DiffCallback
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FoodAdapter.FoodDataViewHolder {
        return FoodDataViewHolder(
            FoodListViewBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(
        holder: FoodAdapter.FoodDataViewHolder,
        position: Int
    ) {
        val foodProperty = getItem(position)
        holder.bind(foodProperty)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(foodProperty)
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Food>() {
        override fun areItemsTheSame(
            oldItem: Food,
            newItem: Food
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: Food,
            newItem: Food
        ): Boolean {
            return oldItem.food_id == newItem.food_id
        }
    }

    class FoodDataViewHolder(private var binding: FoodListViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(foodProperty: Food) {
            binding.property = foodProperty
            binding.executePendingBindings()
        }
    }

    class OnClickListener(val clickListener: (foodProperty: Food) -> Unit) {
        fun onClick(foodProperty: Food) = clickListener(foodProperty)
    }
}