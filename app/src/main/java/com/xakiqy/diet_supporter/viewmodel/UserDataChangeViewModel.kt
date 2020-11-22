package com.xakiqy.diet_supporter.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.xakiqy.diet_supporter.database.User
import com.xakiqy.diet_supporter.database.UserPersonalDataHistory
import com.xakiqy.diet_supporter.database.getDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class UserDataChangeViewModel(context: Context) : ViewModel() {
    private val job = Job()
    private val ioScope = CoroutineScope(Dispatchers.IO + job)

    private val database = getDatabase(context)

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

    class Factory(private val context: Context) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UserDataChangeViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return UserDataChangeViewModel(context) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}