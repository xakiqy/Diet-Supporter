package com.example.diet_supporter.ui.diet

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.example.diet_supporter.R
import com.example.diet_supporter.adapter.PersonalFoodAdapter
import com.example.diet_supporter.databinding.FragmentTabPersonalFoodBinding
import com.example.diet_supporter.viewmodel.PersonalFoodTabViewModel

class PersonalFoodTabFragment : Fragment() {

    private var isSearch = true

    private val viewModel: PersonalFoodTabViewModel by lazy {
        ViewModelProvider(this, PersonalFoodTabViewModel.Factory(requireContext())).get(
            PersonalFoodTabViewModel::class.java
        )
    }

    private lateinit var binding: FragmentTabPersonalFoodBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentTabPersonalFoodBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewModel.allFoodLoad.observe(viewLifecycleOwner, {
            viewModel.foodData.value = it
        })

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
        binding.viewPersonalFoodList.adapter =
            PersonalFoodAdapter(PersonalFoodAdapter.OnClickListener { it, image ->
                val extras = FragmentNavigatorExtras(image to getString(R.string.transitionImg))

                val directions =
                    PersonalFoodTabFragmentDirections.actionPersonalFoodTabFragmentToPersonalFoodConsumeFragment(
                        it
                    )
                findNavController().navigate(directions, extras)
            })
        setHasOptionsMenu(true)

        binding.searchFoodView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchFoodView.clearFocus()

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (isSearch) {
                    viewModel.searchFood(newText!!)
                }
                isSearch = true
                return true
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_with_add, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        val navController = findNavController()
        when (id) {
            R.id.personalFoodRegisterFragment -> navController.navigate(
                PersonalFoodTabFragmentDirections.actionPersonalFoodTabFragmentToRegisterPersonalFoodFragment()
            )
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPause() {
        super.onPause()
        isSearch = false
    }
}