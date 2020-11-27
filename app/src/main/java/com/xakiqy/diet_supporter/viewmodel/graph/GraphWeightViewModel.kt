package com.xakiqy.diet_supporter.viewmodel.graph

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.xakiqy.diet_supporter.database.UserDietDatabase

class GraphWeightViewModel @ViewModelInject constructor(private val database: UserDietDatabase) :
    ViewModel() {
    val userDataHistory = database.userPersonalDataHistoryDao.getLoadUserHistories()
}