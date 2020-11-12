package com.example.diet_supporter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.diet_supporter.database.FoodAte
import com.example.diet_supporter.databinding.FoodAteListViewBinding

class FoodAteAdapter(val onClickListener: OnClickListener) :
    ListAdapter<FoodAte, FoodAteAdapter.FoodDataViewHolder>(
        DiffCallback
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FoodAteAdapter.FoodDataViewHolder {
        return FoodDataViewHolder(
            FoodAteListViewBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(
        holder: FoodAteAdapter.FoodDataViewHolder,
        position: Int
    ) {
        val foodProperty = getItem(position)
        holder.bind(foodProperty)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(foodProperty)
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<FoodAte>() {
        override fun areItemsTheSame(
            oldItem: FoodAte,
            newItem: FoodAte
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: FoodAte,
            newItem: FoodAte
        ): Boolean {
            return oldItem.food_id == newItem.food_id
        }



    }

    class FoodDataViewHolder(private var binding: FoodAteListViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(foodProperty: FoodAte) {
            binding.property = foodProperty
            binding.executePendingBindings()
        }
    }

    class OnClickListener(val clickListener: (foodProperty: FoodAte) -> Unit) {
        fun onClick(foodProperty: FoodAte) = clickListener(foodProperty)
    }
}