package com.xakiqy.diet_supporter.ui.diet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.xakiqy.diet_supporter.R
import com.xakiqy.diet_supporter.database.User
import com.xakiqy.diet_supporter.databinding.FragmentPostRegisterBinding
import com.xakiqy.diet_supporter.util.activityOfSpinnerPosition
import com.xakiqy.diet_supporter.util.activityToSpinnerPosition
import com.xakiqy.diet_supporter.util.difficultyOfSpinnerPosition
import com.xakiqy.diet_supporter.util.difficultyToSpinnerPosition
import com.xakiqy.diet_supporter.viewmodel.UserDietChangeViewModel

class UserDietChangeFragment : Fragment() {
    private val viewModel: UserDietChangeViewModel by lazy {
        ViewModelProvider(this, UserDietChangeViewModel.Factory(requireContext())).get(
            UserDietChangeViewModel::class.java
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentPostRegisterBinding.inflate(inflater)

        binding.textAverageActivity.visibility = View.INVISIBLE
        binding.spinnerPhysicalActivity.onItemSelectedListener =
            viewModel.activitySpinnerListener(binding)

        viewModel.user.observe(viewLifecycleOwner, {
            binding.spinnerPhysicalActivity.setSelection(activityToSpinnerPosition(it.physicalActivity!!))
            binding.spinnerDietDifficulty.setSelection(difficultyToSpinnerPosition(it.dietDifficulty!!))
        })

        binding.buttonStartDiet.setOnClickListener {
            viewModel.updateUser(
                User(
                    viewModel.user.value!!.id,
                    viewModel.user.value!!.name,
                    viewModel.user.value!!.weight,
                    viewModel.user.value!!.height,
                    viewModel.user.value!!.age,
                    viewModel.user.value!!.gender,
                    difficultyOfSpinnerPosition(binding.spinnerDietDifficulty.selectedItemPosition),
                    activityOfSpinnerPosition(binding.spinnerPhysicalActivity.selectedItemPosition)
                )
            )
            val toast = Toast.makeText(
                context,
                resources.getText(R.string.toast_user_data_change),
                Toast.LENGTH_LONG
            )
            toast.show()
        }

        return binding.root
    }
}