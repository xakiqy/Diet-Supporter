package com.xakiqy.diet_supporter.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.xakiqy.diet_supporter.database.FoodAte
import com.xakiqy.diet_supporter.database.PersonalFood
import com.xakiqy.diet_supporter.database.getDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PersonalFoodConsumeViewModel(context: Context) : ViewModel() {
    private val job = Job()
    private val ioScope = CoroutineScope(Dispatchers.IO + job)

    private val database = getDatabase(context)
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

    class Factory(private val context: Context) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PersonalFoodConsumeViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return PersonalFoodConsumeViewModel(context) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}