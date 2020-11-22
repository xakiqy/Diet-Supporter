package com.xakiqy.diet_supporter.ui.diet

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.transition.TransitionInflater
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.xakiqy.diet_supporter.R
import com.xakiqy.diet_supporter.database.PersonalFood
import com.xakiqy.diet_supporter.databinding.FragmentPersonalFoodConsumeBinding
import com.xakiqy.diet_supporter.viewmodel.PersonalFoodConsumeViewModel
import java.util.concurrent.TimeUnit

class PersonalFoodConsumeFragment : Fragment() {

    private lateinit var binding: FragmentPersonalFoodConsumeBinding

    private val viewModel: PersonalFoodConsumeViewModel by lazy {
        ViewModelProvider(this, PersonalFoodConsumeViewModel.Factory(requireContext())).get(
            PersonalFoodConsumeViewModel::class.java
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        postponeEnterTransition(200, TimeUnit.MILLISECONDS)
        binding = FragmentPersonalFoodConsumeBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel.foodAte.value =
            PersonalFoodConsumeFragmentArgs.fromBundle(requireArguments()).personalFood

        sharedElementEnterTransition =
            TransitionInflater.from(this.context).inflateTransition(R.transition.move)

        sharedElementReturnTransition =
            TransitionInflater.from(this.context).inflateTransition(R.transition.move)

        binding.viewModel = viewModel

        viewModel.dietHistory.observe(viewLifecycleOwner, {})

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

                viewModel.foodAte.value = PersonalFood(
                    food.food_id,
                    food.food_description,
                    food.energy.times(coef),
                    food.protein.times(coef),
                    food.carbohydrate.times(coef),
                    food.fat.times(coef),
                    grams.toInt(),
                    food.pathToImg
                )
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.buttonAddFood.setOnClickListener {
            viewModel.addFood()
            findNavController().navigateUp()
        }

        binding.buttonDelete.setOnClickListener {
            val alertDialog: AlertDialog? = activity?.let {
                val builder = AlertDialog.Builder(it)
                builder.apply {
                    setMessage(R.string.label_dialog_realy)
                    setPositiveButton(
                        R.string.yes,
                        DialogInterface.OnClickListener { dialog, id ->
                            viewModel.deleteFood()
                            findNavController().navigateUp()
                        })
                    setNegativeButton(R.string.no,
                        DialogInterface.OnClickListener { dialog, id ->
                            // User cancelled the dialog
                        })

                }
                builder.create()
            }
            alertDialog!!.show()
            val positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            val parent = positiveButton.parent as LinearLayout
            parent.gravity = Gravity.CENTER_HORIZONTAL
            val leftSpacer = parent.getChildAt(1)
            leftSpacer.visibility = View.GONE
        }
        }
}