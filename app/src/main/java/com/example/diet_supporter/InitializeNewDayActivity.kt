package com.example.diet_supporter

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.diet_supporter.database.DietHistory
import com.example.diet_supporter.database.getDatabase
import com.example.diet_supporter.database.toLocalUser
import com.example.diet_supporter.databinding.ActivityInitializeNewDayBinding
import com.example.diet_supporter.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class InitializeNewDayActivity : AppCompatActivity() {
    private val job = Job()
    private val ioScope = CoroutineScope(Dispatchers.IO + job)

    private val database = getDatabase(this)
    private val user = database.userDao.getLoadUser()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initialize_new_day)

        val binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_initialize_new_day
        ) as ActivityInitializeNewDayBinding

        ArrayAdapter.createFromResource(
            this,
            R.array.today_activity,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            binding.spinner.adapter = adapter
        }
        user.observe(this, {
            binding.spinner.setSelection(it.physicalActivity!!.ordinal)
        })

        binding.buttonStartNewDay.setOnClickListener {
            val caloriesNeed = getCaloriesNeed(user.value!!.toLocalUser())
            val newDietHistory = DietHistory(
                getDateTodayWithoutTime(),
                user.value!!.id!!,
                caloriesNeed,
                getProteinNeed(caloriesNeed),
                getCarbohydrateNeed(caloriesNeed),
                getFatNeed(caloriesNeed)
            )
            ioScope.launch {
                database.dietHistoryDao.insertHistory(newDietHistory)
            }
            startActivity(Intent(this, DietActivity::class.java))
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}