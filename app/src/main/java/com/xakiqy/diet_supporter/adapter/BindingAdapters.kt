package com.xakiqy.diet_supporter.adapter

import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.xakiqy.diet_supporter.R
import com.xakiqy.diet_supporter.database.Food
import com.xakiqy.diet_supporter.database.FoodAte
import com.xakiqy.diet_supporter.database.PersonalFood

@BindingAdapter("foodListData")
fun bindFoodRecyclerView(recyclerView: RecyclerView, data: List<Food>?) {
    val adapter = recyclerView.adapter as FoodAdapter
    adapter.submitList(data)
}

@BindingAdapter("foodAteListData")
fun bindFoodAteRecyclerView(recyclerView: RecyclerView, data: List<FoodAte>?) {
    val adapter = recyclerView.adapter as FoodAteAdapter
    adapter.submitList(data)
}

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    Glide.with(imgView.context)
        .load(imgUrl)
        .apply(
            RequestOptions()
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_broken_image)
        )
        .into(imgView)
}

@BindingAdapter("numberDoubleFormat")
fun bindNumberDoubleFormat(textView: TextView, number: Double){
    textView.text = String.format("%.1f", number)
}

@BindingAdapter("imageUrlBase")
fun bindImageBase(imgView: ImageView, imgUrl: String?) {
    Glide.with(imgView.context)
        .load(imgUrl)
        .apply(
            RequestOptions()
                .error(R.drawable.ic_broken_image)
        ).dontTransform()
        .into(imgView)

}

@BindingAdapter("personalFoodListData")
fun bindPersonalFoodRecyclerView(recyclerView: RecyclerView, data: List<PersonalFood>?) {
    val adapter = recyclerView.adapter as PersonalFoodAdapter
    adapter.submitList(data)
}
