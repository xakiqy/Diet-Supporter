package com.xakiqy.diet_supporter.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(
    entities = [User::class, UserPersonalDataHistory::class, DietHistory::class, Food::class, FoodAte::class, PersonalFood::class,
        Factor::class], version = 2, exportSchema = false
)
@TypeConverters(
    DietDifficultyConverter::class,
    PhysicalActivityConverter::class,
    DateConverter::class,
    GenderConverter::class
)
abstract class UserDietDatabase : RoomDatabase() {
    abstract val personalFoodDao: PersonalFoodDao
    abstract val userDao: UserDao
    abstract val userPersonalDataHistoryDao: UserPersonalDataHistoryDao
    abstract val dietHistoryDao: DietHistoryDao
    abstract val foodDao: FoodDao
    abstract val foodAteDao: FoodAteDao
    abstract val factorDao: FactorDao
}
