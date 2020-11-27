package com.xakiqy.diet_supporter.ui.register

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.xakiqy.diet_supporter.PreLoadActivity
import com.xakiqy.diet_supporter.database.User
import com.xakiqy.diet_supporter.databinding.FragmentPostRegisterBinding
import com.xakiqy.diet_supporter.util.activityOfSpinnerPosition
import com.xakiqy.diet_supporter.util.difficultyOfSpinnerPosition
import com.xakiqy.diet_supporter.viewmodel.PostRegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostRegisterFragment : Fragment() {

    private val viewModel by viewModels<PostRegisterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentPostRegisterBinding.inflate(inflater)
        val user = PostRegisterFragmentArgs.fromBundle(requireArguments()).user

        binding.spinnerPhysicalActivity.onItemSelectedListener =
            viewModel.activitySpinnerListener(binding)

        binding.buttonStartDiet.setOnClickListener {
            viewModel.registerUserDiet(
                User(
                    user.name, user.weight, user.height, user.age, user.gender,
                    difficultyOfSpinnerPosition(binding.spinnerDietDifficulty.selectedItemPosition),
                    activityOfSpinnerPosition(binding.spinnerPhysicalActivity.selectedItemPosition)
                )
            )
            startActivity(Intent(requireContext(), PreLoadActivity::class.java))
            activity?.finish()
        }
        return binding.root
    }

}