package com.example.diet_supporter.database

import android.content.Context
import androidx.room.*


@Database(entities = [User::class, UserPersonalDataHistory::class, DietHistory::class, Food::class, FoodAte::class, PersonalFood::class], version = 2, exportSchema = false)
@TypeConverters(DietDifficultyConverter::class, PhysicalActivityConverter::class, DateConverter::class, GenderConverter::class)
abstract class UserDietDatabase : RoomDatabase() {
    abstract val personalFoodDao : PersonalFoodDao
    abstract val userDao: UserDao
    abstract val userPersonalDataHistoryDao: UserPersonalDataHistoryDao
    abstract val dietHistoryDao: DietHistoryDao
    abstract val foodDao : FoodDao
    abstract val foodAteDao : FoodAteDao
}

private lateinit var INSTANCE: UserDietDatabase

fun getDatabase(context: Context): UserDietDatabase {
    synchronized(UserDietDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                UserDietDatabase::class.java,
                "user_diet")
                .createFromAsset("db_food_en.db")
//                .fallbackToDestructiveMigration()
                .build()
        }
    }
    return INSTANCE
}