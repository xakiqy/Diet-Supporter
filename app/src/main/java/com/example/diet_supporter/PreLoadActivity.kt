package com.example.diet_supporter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.diet_supporter.database.DietHistory
import com.example.diet_supporter.database.User
import com.example.diet_supporter.database.UserDietDatabase
import com.example.diet_supporter.database.getDatabase
import com.example.diet_supporter.util.*
import kotlinx.coroutines.*

class PreLoadActivity : AppCompatActivity() {
    private val job = Job()
    private val ioScope = CoroutineScope(Dispatchers.IO + job)

    lateinit var database: UserDietDatabase
    lateinit var user: LiveData<User>
    private val dietHistory =  MutableLiveData<DietHistory>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_load)

        database = getDatabase(this)

        user = database.userDao.getLoadUser()

        user.observe(this, {
            if (it == null) {
                startActivity(Intent(this, RegisterActivity::class.java))
                finish()
            } else {
                getDietHistory()
            }
        })

        dietHistory.observe(this, {
            if (it == null) {
                startActivity(Intent(this, InitializeNewDayActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, DietActivity::class.java))
                finish()
            }
        })
    }

    private fun getDietHistory() {
        ioScope.launch {
            val localDh =
                database.dietHistoryDao.getLastDietHistory()
            withContext(Dispatchers.Main){
                dietHistory.value = localDh
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}