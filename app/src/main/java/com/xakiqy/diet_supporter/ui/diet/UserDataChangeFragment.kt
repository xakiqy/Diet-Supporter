package com.xakiqy.diet_supporter.ui.diet

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.xakiqy.diet_supporter.R
import com.xakiqy.diet_supporter.database.User
import com.xakiqy.diet_supporter.databinding.FragmentRegisterBinding
import com.xakiqy.diet_supporter.util.genderToLong
import com.xakiqy.diet_supporter.util.longToGender
import com.xakiqy.diet_supporter.viewmodel.UserDataChangeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserDataChangeFragment : Fragment() {

    private val viewModel by viewModels<UserDataChangeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentRegisterBinding.inflate(inflater)

        viewModel.user.observe(viewLifecycleOwner, {
            binding.editAgeRegister.setText((it.age).toString())
            binding.editHeightRegister.setText((it.height).toString())
            binding.editWeightRegister.setText(it.weight.toString())
            binding.editNameRegister.setText(it.name)
            binding.spinnerGender.setSelection(genderToLong(it.gender).toInt())
        })

        binding.buttonSubmitRegister.setOnClickListener {
            if (isCorrectInputData(binding)) {
                viewModel.updateUser(
                    User(
                        viewModel.user.value!!.id,
                        binding.editNameRegister.text.toString(),
                        binding.editWeightRegister.text.toString().toDouble(),
                        binding.editHeightRegister.text.toString().toInt(),
                        binding.editAgeRegister.text.toString().toInt(),
                        longToGender(binding.spinnerGender.selectedItemId),
                        viewModel.user.value!!.dietDifficulty,
                        viewModel.user.value!!.physicalActivity
                    )
                )
                val toast = Toast.makeText(
                    context,
                    resources.getText(R.string.toast_user_data_change),
                    Toast.LENGTH_LONG
                )
                toast.show()
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