package com.xakiqy.diet_supporter.ui.diet

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.xakiqy.diet_supporter.R
import com.xakiqy.diet_supporter.adapter.FoodAdapter
import com.xakiqy.diet_supporter.database.FoodAte
import com.xakiqy.diet_supporter.databinding.FragmentTabFoodBinding
import com.xakiqy.diet_supporter.viewmodel.FoodTabViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FoodTabFragment : Fragment() {

    companion object {
        private const val defaultWeight = 100
    }

    private val viewModel by viewModels<FoodTabViewModel>()

    private lateinit var binding: FragmentTabFoodBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTabFoodBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner


        binding.viewFoodList.adapter = FoodAdapter(FoodAdapter.OnClickListener {
            viewModel.foodAte.value = FoodAte(
                it.food_description, it.energy, it.protein, it.carbohydrate, it.carbohydrate,
                defaultWeight, viewModel.dietHistory.value!!.id
            )
            binding.motionLayout.setTransition(R.id.onFoodClick)
            binding.motionLayout.transitionToEnd()
            binding.editTextGrams.setText(defaultWeight.toString())
        })

        viewModel.dietHistory.observe(viewLifecycleOwner, {})

        binding.editTextGrams.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.textFoodAte.text.ifEmpty { return }
                var value = s
                if (s.isNullOrEmpty() || s.toString().startsWith("0")) {
                    value = "1"
                }
                val grams = value.toString().toDouble()
                val food = viewModel.foodAte.value
                val coef = grams / food!!.weight

                viewModel.foodAte.value = FoodAte(
                    food.food_description,
                    food.energy.times(coef),
                    food.protein.times(coef),
                    food.carbohydrate.times(coef),
                    food.fat.times(coef),
                    grams.toInt(),
                    viewModel.dietHistory.value!!.id
                )
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.searchFoodView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchFoodView.clearFocus()

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.searchFood(newText!!)
                return true
            }
        })

        binding.buttonAddFood.setOnClickListener {
            viewModel.addFood()
            binding.motionLayout.transitionToStart()
        }

        binding.imageButtonBack.setOnClickListener {
            binding.motionLayout.transitionToStart()
        }

        return binding.root
    }
}