package com.xakiqy.diet_supporter.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.xakiqy.diet_supporter.database.User
import com.xakiqy.diet_supporter.database.UserDietDatabase
import com.xakiqy.diet_supporter.database.UserPersonalDataHistory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class UserDataChangeViewModel @ViewModelInject constructor(private val database: UserDietDatabase) :
    ViewModel() {
    private val job = Job()
    private val ioScope = CoroutineScope(Dispatchers.IO + job)

    val user = database.userDao.getLoadUser()

    fun updateUser(user: User) {
        ioScope.launch {
            database.userDao.updateUser(user)
            addUserDataHistoryChange(user)
        }
    }

    private fun addUserDataHistoryChange(user: User) {
        ioScope.launch {
            database.userPersonalDataHistoryDao.insertHistory(
                UserPersonalDataHistory(
                    user.name,
                    user.weight,
                    user.height,
                    user.age,
                    user.gender,
                    user.dietDifficulty!!,
                    user.physicalActivity!!,
                    user.id
                )
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}