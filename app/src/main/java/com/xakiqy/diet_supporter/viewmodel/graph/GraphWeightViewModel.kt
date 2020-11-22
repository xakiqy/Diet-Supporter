package com.xakiqy.diet_supporter.viewmodel.graph

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.xakiqy.diet_supporter.database.getDatabase

class GraphWeightViewModel(context: Context) : ViewModel() {

    private val database = getDatabase(context)

    val userDataHistory = database.userPersonalDataHistoryDao.getLoadUserHistories()

    class Factory(private val context: Context) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(GraphWeightViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return GraphWeightViewModel(context) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}