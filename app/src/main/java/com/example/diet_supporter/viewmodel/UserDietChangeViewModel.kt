package com.example.diet_supporter.viewmodel

import android.content.Context
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.diet_supporter.R
import com.example.diet_supporter.database.User
import com.example.diet_supporter.database.getDatabase
import com.example.diet_supporter.databinding.FragmentPostRegisterBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class UserDietChangeViewModel(context: Context) : ViewModel() {
    val res = context
    private val job = Job()
    private val ioScope = CoroutineScope(Dispatchers.IO + job)

    private val database = getDatabase(context)

    val user = database.userDao.getLoadUser()

    fun updateUser(user: User) {
        ioScope.launch {
            database.userDao.updateUser(user)
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
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
                binding.textAverageActivity.visibility = View.VISIBLE
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

    class Factory(private val context: Context) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UserDietChangeViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return UserDietChangeViewModel(context) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}