package com.xakiqy.diet_supporter.ui.diet

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.drawToBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.xakiqy.diet_supporter.R
import com.xakiqy.diet_supporter.database.PersonalFood
import com.xakiqy.diet_supporter.databinding.FragmentRegisterPersonalFoodBinding
import com.xakiqy.diet_supporter.viewmodel.RegisterPersonalFoodViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterPersonalFoodFragment : Fragment() {

    private val viewModel by viewModels<RegisterPersonalFoodViewModel>()

    companion object {
        private val myOptions = RequestOptions()
            .override(750, 750).centerCrop().placeholder(R.drawable.loading_animation)
            .error(R.drawable.ic_broken_image)
        private const val IMAGE_PICK_CODE = 1000
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentRegisterPersonalFoodBinding.inflate(inflater)

        binding.buttonRegisterFood.setOnClickListener {
            if (isCorrectInputData(binding)) {
                val imageName = System.currentTimeMillis().toString() + ".png"
                requireContext().openFileOutput(imageName, Context.MODE_PRIVATE).use {
                    binding.foodImage.drawToBitmap().compress(Bitmap.CompressFormat.PNG, 100, it)
                }
                viewModel.pathToImg = requireContext().filesDir.absolutePath + "/" + imageName

                viewModel.addPersonalFood(
                    PersonalFood(
                        binding.editTextFoodDesc.text.toString(),
                        binding.editTextCalories.text.toString().toDouble(),
                        binding.editTextProtein.text.toString().toDouble(),
                        binding.editTextCarbs.text.toString().toDouble(),
                        binding.editTextFat.text.toString().toDouble(),
                        100,
                        viewModel.pathToImg
                    )
                )
                findNavController().navigateUp()
            }
        }

        binding.foodImage.setOnClickListener {
            pickImageFromGallery()
        }

        viewModel.imageUri.observe(viewLifecycleOwner, {
            Glide
                .with(this)
                .asBitmap()
                .apply(myOptions)
                .load(it)
                .into(binding.foodImage)
        })
        return binding.root
    }

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    //handle result of picked image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            viewModel.imageUri.value = data?.data
        }
    }

    private fun isCorrectInputData(binding: FragmentRegisterPersonalFoodBinding): Boolean {
        var correct = true
        with(binding) {
            try {
                editTextCalories.text.toString().toDouble()
            } catch (e: Exception) {
                editTextCalories.setBackgroundColor(Color.RED)
                editTextCalories.text = null
                editTextCalories.hint = resources.getString(R.string.hint_food_register)
                correct = false
            }
            try {
                editTextCarbs.text.toString().toDouble()
            } catch (e: Exception) {
                editTextCarbs.setBackgroundColor(Color.RED)
                editTextCarbs.text = null
                editTextCarbs.hint = resources.getString(R.string.hint_food_register)
                correct = false
            }
            try {
                editTextFat.text.toString().toDouble()
            } catch (e: Exception) {
                editTextFat.setBackgroundColor(Color.RED)
                editTextFat.text = null
                editTextFat.hint = resources.getString(R.string.hint_food_register)
                correct = false
            }
            try {
                editTextProtein.text.toString().toDouble()
            } catch (e: Exception) {
                editTextProtein.setBackgroundColor(Color.RED)
                editTextProtein.text = null
                editTextProtein.hint = resources.getString(R.string.hint_food_register)
                correct = false
            }
        }
        return correct
    }
}