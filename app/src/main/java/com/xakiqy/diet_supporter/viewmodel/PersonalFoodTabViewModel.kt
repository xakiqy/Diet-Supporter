package com.xakiqy.diet_supporter.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xakiqy.diet_supporter.database.PersonalFood
import com.xakiqy.diet_supporter.database.UserDietDatabase
import kotlinx.coroutines.*

class PersonalFoodTabViewModel @ViewModelInject constructor(private val database: UserDietDatabase) :
    ViewModel() {
    private val job = Job()
    private val ioScope = CoroutineScope(Dispatchers.IO + job)

    val allFoodLoad = database.personalFoodDao.getLoadAllPersonalFood()

    val foodData = MutableLiveData<List<PersonalFood>>()

    fun searchFood(textSearch: String) {
        ioScope.launch {
            val food = database.personalFoodDao.getFoodByDescription(textSearch)
            withContext(Dispatchers.Main) {
                foodData.value = food
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}