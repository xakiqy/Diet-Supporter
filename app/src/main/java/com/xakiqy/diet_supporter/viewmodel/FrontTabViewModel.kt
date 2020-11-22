package com.xakiqy.diet_supporter.viewmodel

import android.content.Context
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.xakiqy.diet_supporter.R
import com.xakiqy.diet_supporter.database.DietHistoryWithFoodAte
import com.xakiqy.diet_supporter.database.FoodAte
import com.xakiqy.diet_supporter.database.getDatabase
import com.xakiqy.diet_supporter.databinding.FragmentTabDietBinding
import com.xakiqy.diet_supporter.util.dipToFloat
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.jetbrains.annotations.NotNull
import timber.log.Timber
import java.text.SimpleDateFormat
import kotlin.math.abs

class FrontTabViewModel(context: Context) : ViewModel() {
    private val job = Job()
    private val ioScope = CoroutineScope(Dispatchers.IO + job)
    private val database = getDatabase(context)

    val user = database.userDao.getLoadUser()

    private var needForGraph = 0
    private var doneForGraph = 0

    val dietHistoryWithFoodAte =
        database.dietHistoryDao.getLoadLastDietHistoryWithFoodAte()

    fun deleteFood(foodAte: FoodAte) {
        ioScope.launch {
            database.foodAteDao.deleteFoodAte(foodAte)
        }
    }

    fun nextGraph(
        dietHistory: DietHistoryWithFoodAte,
        binding: @NotNull FragmentTabDietBinding,
        resources: Context
    ) {
        needForGraph++
        doneForGraph++
        Timber.i(needForGraph.toString())
        if (needForGraph > 3) {
            needForGraph = 0; doneForGraph = 0
        }
        graphBinding(dietHistory, binding, resources)
    }

    fun graphBinding(
        dietHistory: DietHistoryWithFoodAte,
        binding: @NotNull FragmentTabDietBinding,
        resources: Context
    ) {
        var need =
            getNeedForGraph(dietHistory) - getDoneForGraph(dietHistory)
        val done = getDoneForGraph(dietHistory)
        var needColor = resources.getColor(R.color.blue_aqua)
        var needText = resources.getString(R.string.label_need)
        if (need < 0) {
            need = abs(need)
            needColor = resources.getColor(R.color.red_smooth)
            needText = resources.getString(R.string.label_overused)
        }

        val dataNeedInfoList = listOf<PieEntry>(
            PieEntry(
                need, needText
            ),
            PieEntry(
                done,
                resources.getString(R.string.label_done)
            )
        )
        val dataSet = PieDataSet(dataNeedInfoList, "")
        dataSet.setColors(
            needColor,
            resources.getColor(R.color.green_smooth),
        )
        dataSet.valueTextSize = dipToFloat(resources, R.dimen.graph_text_size)
        binding.chartCalories.data = PieData(dataSet)
        val date = SimpleDateFormat("dd/MM").format(dietHistoryWithFoodAte.value!!.dietHistory.date)
        val desc = Description()
        desc.text = getDescForGraph(resources) + " " + date
        desc.textSize = dipToFloat(resources, R.dimen.graph_text_size)
        binding.chartCalories.description = desc
        binding.chartCalories.legend.isEnabled = false
        binding.chartCalories.animateXY(600, 600)
        binding.chartCalories.invalidate()
        binding.chartCalories.visibility = View.VISIBLE
    }

    private fun getNeedForGraph(dietHistoryWithFoodAte: DietHistoryWithFoodAte): Float {
        when (needForGraph) {
            0 -> return dietHistoryWithFoodAte.dietHistory.caloriesNeed.toFloat()
            1 -> return dietHistoryWithFoodAte.dietHistory.proteinNeed.toFloat()
            2 -> return dietHistoryWithFoodAte.dietHistory.carbohydrateNeed.toFloat()
            3 -> return dietHistoryWithFoodAte.dietHistory.fatNeed.toFloat()
        }
        return dietHistoryWithFoodAte.dietHistory.caloriesNeed.toFloat()
    }

    private fun getDoneForGraph(dietHistoryWithFoodAte: DietHistoryWithFoodAte): Float {
        when (doneForGraph) {
            0 -> return dietHistoryWithFoodAte.foodAte.sumByDouble { it.energy }.toFloat()
            1 -> return dietHistoryWithFoodAte.foodAte.sumByDouble { it.protein }.toFloat()
            2 -> return dietHistoryWithFoodAte.foodAte.sumByDouble { it.carbohydrate }.toFloat()
            3 -> return dietHistoryWithFoodAte.foodAte.sumByDouble { it.fat }.toFloat()
        }
        return dietHistoryWithFoodAte.foodAte.sumByDouble { it.energy }.toFloat()
    }

    private fun getDescForGraph(resources: Context): String {
        when (doneForGraph) {
            0 -> return resources.getString(R.string.label_calories)
            1 -> return resources.getString(R.string.label_protein)
            2 -> return resources.getString(R.string.label_carbohydrate)
            3 -> return resources.getString(R.string.label_fat)
        }
        return resources.getString(R.string.label_calories)
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }


    class Factory(private val context: Context) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FrontTabViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return FrontTabViewModel(context) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}