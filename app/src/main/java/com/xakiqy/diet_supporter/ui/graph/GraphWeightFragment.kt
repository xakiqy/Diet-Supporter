package com.xakiqy.diet_supporter.ui.graph

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.xakiqy.diet_supporter.R
import com.xakiqy.diet_supporter.databinding.FragmentWeightGraphBinding
import com.xakiqy.diet_supporter.util.dipToFloat
import com.xakiqy.diet_supporter.viewmodel.graph.GraphWeightViewModel
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class GraphWeightFragment : Fragment() {

    private val viewModel: GraphWeightViewModel by lazy {
        ViewModelProvider(this, GraphWeightViewModel.Factory(requireContext())).get(
            GraphWeightViewModel::class.java
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentWeightGraphBinding.inflate(inflater)
        val graphTextSize = dipToFloat(requireContext(), R.dimen.graph_diet_text_size)
        viewModel.userDataHistory.observe(viewLifecycleOwner, {
            val lineList = ArrayList<Entry>()

            it.forEachIndexed { index, dh ->
                lineList.add(Entry(index.toFloat(), dh.weight.toFloat()))
            }

            val lineData = LineDataSet(lineList, resources.getString(R.string.label_weight_full))

            lineData.setCircleColor(Color.RED)
            lineData.setDrawCircleHole(false)
            lineData.setDrawValues(false)
            lineData.color = Color.RED

            binding.userWeightGraph.data = LineData(lineData)
            binding.userWeightGraph.xAxis.textSize = graphTextSize
            binding.userWeightGraph.axisRight.textSize = graphTextSize
            binding.userWeightGraph.axisLeft.textSize = graphTextSize
            binding.userWeightGraph.description = Description().apply {
                text = resources.getString(R.string.label_weight_full); textSize = graphTextSize
            }
            binding.userWeightGraph.legend.textSize = graphTextSize

            binding.userWeightGraph.invalidate()
        })

        return binding.root
    }
}