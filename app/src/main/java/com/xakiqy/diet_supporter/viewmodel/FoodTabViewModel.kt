package com.xakiqy.diet_supporter.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xakiqy.diet_supporter.database.Food
import com.xakiqy.diet_supporter.database.FoodAte
import com.xakiqy.diet_supporter.database.UserDietDatabase
import kotlinx.coroutines.*

class FoodTabViewModel @ViewModelInject constructor(private val database: UserDietDatabase) :
    ViewModel() {
    private val job = Job()
    private val ioScope = CoroutineScope(Dispatchers.IO + job)

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
}