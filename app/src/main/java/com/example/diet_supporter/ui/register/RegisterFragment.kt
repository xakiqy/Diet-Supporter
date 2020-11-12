package com.example.diet_supporter.ui.register

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.diet_supporter.R
import com.example.diet_supporter.database.User
import com.example.diet_supporter.databinding.FragmentRegisterBinding
import com.example.diet_supporter.util.longToGender
import java.lang.Exception

class RegisterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentRegisterBinding.inflate(inflater)

        binding.buttonSubmitRegister.setOnClickListener {
            if (isCorrectInputData(binding)) {
                with(binding) {
                    findNavController().navigate(
                        RegisterFragmentDirections.actionRegisterFragmentToPostRegisterFragment(
                            User(
                                editNameRegister.text.toString(),
                                editWeightRegister.text.toString().toDouble(),
                                editHeightRegister.text.toString().toInt(),
                                editAgeRegister.text.toString().toInt(),
                                longToGender(spinnerGender.selectedItemId), null, null
                            )
                        )
                    )
                }
            }
        }
        return binding.root
    }


    private fun isCorrectInputData(binding: FragmentRegisterBinding): Boolean {
        var correct = true
        with(binding) {
            try {
                editWeightRegister.text.toString().toDouble()
            } catch (e: Exception) {
                editWeightRegister.setBackgroundColor(Color.RED)
                editWeightRegister.text = null
                editWeightRegister.hint = resources.getString(R.string.weight_should_be)
                correct = false
            }
            try {
                editHeightRegister.text.toString().toInt()
            } catch (e: Exception) {
                editHeightRegister.setBackgroundColor(Color.RED)
                editHeightRegister.text = null
                editHeightRegister.hint = resources.getString(R.string.height_should_be)
                correct = false
            }
            try {
                editAgeRegister.text.toString().toInt()
            } catch (e: Exception) {
                editAgeRegister.setBackgroundColor(Color.RED)
                editAgeRegister.text = null
                editAgeRegister.hint = resources.getString(R.string.age_should_be)
                correct = false
            }
        }
        return correct
    }
}