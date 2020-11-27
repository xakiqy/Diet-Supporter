package com.xakiqy.diet_supporter.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xakiqy.diet_supporter.database.FoodAte
import com.xakiqy.diet_supporter.database.PersonalFood
import com.xakiqy.diet_supporter.database.UserDietDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PersonalFoodConsumeViewModel @ViewModelInject constructor(private val database: UserDietDatabase) :
    ViewModel() {
    private val job = Job()
    private val ioScope = CoroutineScope(Dispatchers.IO + job)

    val dietHistory = database.dietHistoryDao.getLastLoadDietHistory()
    val foodAte = MutableLiveData<PersonalFood>()

    fun addFood() {
        ioScope.launch {
            database.foodAteDao.insertFoodAte(
                FoodAte(
                    foodAte.value!!.food_description, foodAte.value!!.energy,
                    foodAte.value!!.protein, foodAte.value!!.carbohydrate,
                    foodAte.value!!.fat, foodAte.value!!.weight, dietHistory.value!!.id
                )
            )
        }
    }

    fun deleteFood() {
        ioScope.launch {
            database.personalFoodDao.deletePersonalFood(foodAte.value!!)
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}