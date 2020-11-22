package com.xakiqy.diet_supporter.viewmodel.graph

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.xakiqy.diet_supporter.database.DietHistoryWithFoodAte
import com.xakiqy.diet_supporter.database.getDatabase
import com.xakiqy.diet_supporter.util.DayDirections
import com.xakiqy.diet_supporter.util.FoodEnergy

import kotlinx.coroutines.*
import java.util.*


class GraphDietViewModel(context: Context) : ViewModel() {

    private val job = Job()
    private val ioScope = CoroutineScope(Dispatchers.IO + job)

    private val database = getDatabase(context)

    var graphType = FoodEnergy.Calories

    private var dateFrom = Calendar.getInstance().apply { add(Calendar.DATE, -7) }
    private var dateTo = Calendar.getInstance().apply { add(Calendar.DATE, 0) }

    private val _dietHistory = MutableLiveData<List<DietHistoryWithFoodAte>>()
    val dietHistory: LiveData<List<DietHistoryWithFoodAte>>
        get() = _dietHistory

    init {
        setDietHistoryLiveData(DayDirections.HOLD)
    }

    fun setDietHistoryLiveData(direction: DayDirections) {
        when (direction) {
            DayDirections.NEXT -> nextSevenDays()
            DayDirections.PREVIOUS -> previousSevenDays()
        }
        ioScope.launch {
            val dh = database.dietHistoryDao.getDietHistoryWithFoodBetweenDays(
                dateFrom.time.time,
                dateTo.time.time
            )
            withContext(Dispatchers.Main) {
                if (dh.isEmpty()) {
                    when (direction) {
                        DayDirections.NEXT -> previousSevenDays()
                        DayDirections.PREVIOUS -> nextSevenDays()
                    }
                } else {
                    _dietHistory.value = dh
                }
            }
        }
    }

    private fun nextSevenDays() {
        dateFrom.apply { add(Calendar.DATE, 7) }
        dateTo.apply { add(Calendar.DATE, 7) }
    }

    private fun previousSevenDays() {
        dateFrom.apply { add(Calendar.DATE, -7) }
        dateTo.apply { add(Calendar.DATE, -7) }
    }


    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    class Factory(private val context: Context) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(GraphDietViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return GraphDietViewModel(context) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}