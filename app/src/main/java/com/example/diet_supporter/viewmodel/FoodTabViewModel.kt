package com.example.diet_supporter.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.diet_supporter.database.Food
import com.example.diet_supporter.database.FoodAte
import com.example.diet_supporter.database.getDatabase
import kotlinx.coroutines.*

class FoodTabViewModel(context: Context) : ViewModel() {
    private val job = Job()
    private val ioScope = CoroutineScope(Dispatchers.IO + job)

    private val database = getDatabase(context)

    private val _foodData = MutableLiveData<List<Food>>()
    val foodData: LiveData<List<Food>>
        get() = _foodData

    val dietHistory =
        database.dietHistoryDao.getLastLoadDietHistory()

    val foodAte = MutableLiveData<FoodAte>()

    init {
        ioScope.launch {
            searchFood("")
        }
    }

    fun searchFood(textSearch: String) {
        ioScope.launch {
            val food = database.foodDao.getFoodByDescription(textSearch)
            withContext(Dispatchers.Main) {
                _foodData.value = food
            }
        }
    }

    fun addFood() {
        ioScope.launch {
            database.foodAteDao.insertFoodAte(
                foodAte.value!!
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    class Factory(private val context: Context) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FoodTabViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return FoodTabViewModel(context) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}