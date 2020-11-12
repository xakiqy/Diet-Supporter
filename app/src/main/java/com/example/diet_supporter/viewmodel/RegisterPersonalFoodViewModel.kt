package com.example.diet_supporter.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.diet_supporter.database.PersonalFood
import com.example.diet_supporter.database.getDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RegisterPersonalFoodViewModel(context: Context) : ViewModel() {
    private val job = Job()
    private val ioScope = CoroutineScope(Dispatchers.IO + job)

    private val database = getDatabase(context)

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

    class Factory(private val context: Context) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RegisterPersonalFoodViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return RegisterPersonalFoodViewModel(context) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}