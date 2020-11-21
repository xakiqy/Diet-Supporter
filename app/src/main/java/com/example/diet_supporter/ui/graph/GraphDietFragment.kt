package com.example.diet_supporter.ui.graph

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.diet_supporter.R
import com.example.diet_supporter.database.DietHistoryWithFoodAte
import com.example.diet_supporter.databinding.FragmentDietGraphBinding
import com.example.diet_supporter.util.DayDirections
import com.example.diet_supporter.util.FoodEnergy
import com.example.diet_supporter.util.dipToFloat
import com.example.diet_supporter.viewmodel.graph.GraphDietViewModel
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import org.jetbrains.annotations.NotNull
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class GraphDietFragment : Fragment() {
    private val viewModel: GraphDietViewModel by lazy {
        ViewModelProvider(this, GraphDietViewModel.Factory(requireContext())).get(
            GraphDietViewModel::class.java
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDietGraphBinding.inflate(inflater)

        val graphBinder = GraphBinder(binding, requireContext())

        viewModel.dietHistory.observe(viewLifecycleOwner, { dh ->

            graphBinder.initializeDataByGraphType(
                viewModel.graphType,
                dh.sortedBy { it.dietHistory.date })
        })

        binding.buttonLeft.setOnClickListener { viewModel.setDietHistoryLiveData(DayDirections.PREVIOUS) }

        binding.buttonRight.setOnClickListener { viewModel.setDietHistoryLiveData(DayDirections.NEXT) }

        binding.toggleButtonCalories.setOnClickListener {
            viewModel.graphType = FoodEnergy.Calories

            graphBinder.initializeDataByGraphType(
                viewModel.graphType,
                viewModel.dietHistory.value!!
            )
        }
        binding.toggleButtonProtein.setOnClickListener {
            viewModel.graphType = FoodEnergy.Protein

            graphBinder.initializeDataByGraphType(
                viewModel.graphType,
                viewModel.dietHistory.value!!
            )
        }
        binding.toggleButtonCarbs.setOnClickListener {
            viewModel.graphType = FoodEnergy.Carbs

            graphBinder.initializeDataByGraphType(
                viewModel.graphType,
                viewModel.dietHistory.value!!
            )
        }
        binding.toggleButtonFat.setOnClickListener {
            viewModel.graphType = FoodEnergy.Fat

            graphBinder.initializeDataByGraphType(
                viewModel.graphType,
                viewModel.dietHistory.value!!
            )
        }

        return binding.root
    }


    private class GraphBinder(
        val binding: @NotNull FragmentDietGraphBinding,
        val context: Context
    ) {
        private var listNeed = emptyList<Double>()
        private var listDone = emptyList<Double>()
        private var listTime = emptyList<Date>()
        private lateinit var description: Description
        private var overusedTreshold = 0f
        private var df = SimpleDateFormat("dd/MM")
        private val textSizeForGraph = dipToFloat(context, R.dimen.graph_diet_text_size)
        private val green = context.resources.getColor(R.color.green_smooth)
        private val red = context.resources.getColor(R.color.red_smooth)

        fun initializeDataByGraphType(
            graph: FoodEnergy,
            dietHistory: List<DietHistoryWithFoodAte>
        ) {
            listTime = dietHistory.map { it.dietHistory.date }

            when (graph) {
                FoodEnergy.Calories -> {
                    listNeed = dietHistory.map { it.dietHistory.caloriesNeed }
                    listDone = dietHistory.map { dh -> dh.foodAte.sumByDouble { it.energy } }
                    description = Description().apply {
                        text = context.resources.getString(R.string.label_calories); textSize =
                        textSizeForGraph
                    }
                    overusedTreshold = 75f
                }
                FoodEnergy.Protein -> {
                    listNeed = dietHistory.map { it.dietHistory.proteinNeed }
                    listDone = dietHistory.map { dh -> dh.foodAte.sumByDouble { it.protein } }
                    description = Description().apply {
                        text = context.resources.getString(R.string.label_protein); textSize =
                        textSizeForGraph
                    }
                    overusedTreshold = 10f
                }
                FoodEnergy.Carbs -> {
                    listNeed = dietHistory.map { it.dietHistory.carbohydrateNeed }
                    listDone = dietHistory.map { dh -> dh.foodAte.sumByDouble { it.carbohydrate } }
                    description = Description().apply {
                        text = context.resources.getString(R.string.label_carbohydrate); textSize =
                        textSizeForGraph
                    }
                    overusedTreshold = 10f
                }
                FoodEnergy.Fat -> {
                    listNeed = dietHistory.map { it.dietHistory.fatNeed }
                    listDone = dietHistory.map { dh -> dh.foodAte.sumByDouble { it.fat } }
                    description = Description().apply {
                        text = context.resources.getString(R.string.label_fat); textSize =
                        textSizeForGraph
                    }
                    overusedTreshold = 5f
                }
            }
            barGraphBind()
        }

        private fun barGraphBind() {
            val barList = ArrayList<BarEntry>()
            listDone.forEachIndexed { index, it ->
                var overused = it.minus(listNeed[index]).toFloat()
                if (overused < overusedTreshold) {
                    overused = 0f
                }

                barList.add(
                    BarEntry(
                        index.toFloat(),
                        floatArrayOf(
                            it.toFloat() - overused,
                            overused
                        ),
                    )
                )
            }

            val barDataSet = BarDataSet(barList, "")
            barDataSet.stackLabels = arrayOf(
                context.resources.getString(R.string.label_done),
                context.resources.getString(R.string.label_overused)
            )
            barDataSet.setColors(green, red)
            barDataSet.valueTextSize = textSizeForGraph
            barDataSet.setDrawValues(true)

            if (!binding.userCaloriesGraph.isEmpty) {
                binding.userCaloriesGraph.clearValues()
            }
            binding.userCaloriesGraph.xAxis.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return df.format(listTime[value.toInt()])
                }
            }
            binding.userCaloriesGraph.notifyDataSetChanged()
            binding.userCaloriesGraph.setScaleEnabled(false)
            binding.userCaloriesGraph.legend.textSize = textSizeForGraph
            binding.userCaloriesGraph.setDrawValueAboveBar(false)
            binding.userCaloriesGraph.data = BarData(barDataSet)
            binding.userCaloriesGraph.description = description
            binding.userCaloriesGraph.xAxis.textSize = textSizeForGraph
            binding.userCaloriesGraph.axisRight.textSize = textSizeForGraph
            binding.userCaloriesGraph.axisLeft.textSize = textSizeForGraph
            binding.userCaloriesGraph.animateXY(600, 600)
            binding.userCaloriesGraph.invalidate()
        }
    }
}