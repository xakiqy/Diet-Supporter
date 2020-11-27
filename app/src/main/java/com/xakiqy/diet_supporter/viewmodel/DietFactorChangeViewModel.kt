package com.xakiqy.diet_supporter.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.xakiqy.diet_supporter.database.UserDietDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DietFactorChangeViewModel @ViewModelInject constructor(private val database: UserDietDatabase) :
    ViewModel() {
    private val job = Job()
    private val ioScope = CoroutineScope(Dispatchers.IO + job)

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
}