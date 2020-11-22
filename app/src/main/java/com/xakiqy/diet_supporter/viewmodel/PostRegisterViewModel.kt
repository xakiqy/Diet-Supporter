package com.xakiqy.diet_supporter.viewmodel

import android.content.Context
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.xakiqy.diet_supporter.R
import com.xakiqy.diet_supporter.database.User
import com.xakiqy.diet_supporter.database.UserPersonalDataHistory
import com.xakiqy.diet_supporter.database.getDatabase
import com.xakiqy.diet_supporter.databinding.FragmentPostRegisterBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PostRegisterViewModel(context: Context) : ViewModel() {
    private val res = context
    private val job = Job()
    private val ioScope = CoroutineScope(Dispatchers.IO + job)

    private val database = getDatabase(context)

    fun registerUserDiet(user: User) {
        ioScope.launch {
            val userId = database.userDao.insertUser(user)
            val userFirstData = UserPersonalDataHistory(
                user.name,
                user.weight,
                user.height,
                user.age,
                user.gender,
                user.dietDifficulty!!,
                user.physicalActivity!!,
                userId
            )
            database.userPersonalDataHistoryDao.insertHistory(userFirstData)
        }
    }

    fun activitySpinnerListener(binding: FragmentPostRegisterBinding) =
        object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> binding.textAverageActivity.text =
                        res.getString(R.string.physical_activity_hint_sedentary)
                    1 -> binding.textAverageActivity.text =
                        res.getString(R.string.physical_activity_hint_low)
                    2 -> binding.textAverageActivity.text =
                        res.getString(R.string.physical_activity_hint_light)
                    3 -> binding.textAverageActivity.text =
                        res.getString(R.string.physical_activity_hint_medium)
                    4 -> binding.textAverageActivity.text =
                        res.getString(R.string.physical_activity_hint_heavy)
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
            if (modelClass.isAssignableFrom(PostRegisterViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return PostRegisterViewModel(context) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}