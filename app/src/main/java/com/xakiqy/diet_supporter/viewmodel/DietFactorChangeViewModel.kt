package com.xakiqy.diet_supporter.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.xakiqy.diet_supporter.database.getDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DietFactorChangeViewModel(context: Context) : ViewModel() {
    private val job = Job()
    private val ioScope = CoroutineScope(Dispatchers.IO + job)

    private val database = getDatabase(context)

    val factor = database.factorDao.getLoadFactor()

    fun updateFactor(isCustom: Boolean) {
        factor.value!!.apply {
            custom = if (isCustom && calories > 0) {
                1
            } else {
                0
            }
        }
        ioScope.launch {
            database.factorDao.updateFactor(factor.value!!)
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    class Factory(private val context: Context) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DietFactorChangeViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DietFactorChangeViewModel(context) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}