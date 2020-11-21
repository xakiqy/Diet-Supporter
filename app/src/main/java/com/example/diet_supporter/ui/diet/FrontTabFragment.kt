package com.example.diet_supporter.ui.diet

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.diet_supporter.InitializeNewDayActivity
import com.example.diet_supporter.R
import com.example.diet_supporter.adapter.FoodAteAdapter
import com.example.diet_supporter.databinding.FragmentTabDietBinding
import com.example.diet_supporter.util.getDateTodayWithoutTime
import com.example.diet_supporter.viewmodel.FrontTabViewModel

class FrontTabFragment : Fragment() {

    private val viewModel: FrontTabViewModel by lazy {
        ViewModelProvider(this, FrontTabViewModel.Factory(requireContext())).get(
            FrontTabViewModel::class.java
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentTabDietBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.recycleFoodAte.adapter = FoodAteAdapter(FoodAteAdapter.OnClickListener { food ->
            val alertDialog: AlertDialog? = activity?.let {
                val builder = AlertDialog.Builder(it)
                builder.apply {
                    setMessage(R.string.label_dialog_realy)
                    setPositiveButton(
                        R.string.yes,
                        DialogInterface.OnClickListener { dialog, id ->
                            viewModel.deleteFood(food)
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
        })


        viewModel.dietHistoryWithFoodAte.observe(viewLifecycleOwner, {
            viewModel.graphBinding(it, binding, requireContext())
        })

        binding.buttonNextType.setOnClickListener {
            viewModel.nextGraph(
                viewModel.dietHistoryWithFoodAte.value!!,
                binding,
                requireContext()
            )
        }
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.initializeNewDayActivity ->
                if (viewModel.dietHistoryWithFoodAte.value!!.dietHistory.date == getDateTodayWithoutTime()) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.toast_text_today_been_created),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    startActivity(Intent(requireContext(), InitializeNewDayActivity::class.java))
                }
        }
        return super.onOptionsItemSelected(item)
    }
}