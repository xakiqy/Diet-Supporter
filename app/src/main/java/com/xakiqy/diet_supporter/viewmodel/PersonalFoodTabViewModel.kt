package com.xakiqy.diet_supporter.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.xakiqy.diet_supporter.database.PersonalFood
import com.xakiqy.diet_supporter.database.getDatabase
import kotlinx.coroutines.*

class PersonalFoodTabViewModel(context: Context) : ViewModel() {
    private val job = Job()
    private val ioScope = CoroutineScope(Dispatchers.IO + job)

    private val database = getDatabase(context)

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

    class Factory(private val context: Context) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PersonalFoodTabViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return PersonalFoodTabViewModel(context) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}