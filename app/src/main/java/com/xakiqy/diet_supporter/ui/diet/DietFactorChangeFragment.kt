package com.xakiqy.diet_supporter.ui.diet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.xakiqy.diet_supporter.R
import com.xakiqy.diet_supporter.databinding.FragmentFactorBinding
import com.xakiqy.diet_supporter.viewmodel.DietFactorChangeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DietFactorChangeFragment : Fragment() {

    private val viewModel by viewModels<DietFactorChangeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentFactorBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewModel.factor.observe(viewLifecycleOwner, {
            if (it.custom == 1) {
                binding.switchCustom.isChecked = true
            }
        })

        binding.switchCustom.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.motionLayout.transitionToEnd()
            } else {
                binding.motionLayout.transitionToStart()
            }
        }

        binding.buttonSubmit.setOnClickListener {
            viewModel.updateFactor(binding.switchCustom.isChecked)
            Toast.makeText(requireContext(), getString(R.string.updated), Toast.LENGTH_SHORT).show()
        }
        return binding.root
    }
}