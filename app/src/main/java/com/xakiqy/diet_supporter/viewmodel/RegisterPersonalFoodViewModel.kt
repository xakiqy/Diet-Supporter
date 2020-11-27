package com.xakiqy.diet_supporter.viewmodel

import android.net.Uri
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xakiqy.diet_supporter.database.PersonalFood
import com.xakiqy.diet_supporter.database.UserDietDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RegisterPersonalFoodViewModel @ViewModelInject constructor(private val database: UserDietDatabase) :
    ViewModel() {
    private val job = Job()
    private val ioScope = CoroutineScope(Dispatchers.IO + job)

    val imageUri = MutableLiveData<Uri>()

    var pathToImg = String()

    fun addPersonalFood(food: PersonalFood) {
        ioScope.launch {
            database.personalFoodDao.insertPersonalFood(food)
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}