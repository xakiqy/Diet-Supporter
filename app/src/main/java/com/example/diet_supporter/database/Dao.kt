package com.example.diet_supporter.database

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*

@Dao
interface UserDao {
    @Query("select * from user ORDER BY ID DESC")
    fun getUsers(): List<User>

    @Query("select * from user where id =:userId")
    fun getUserById(userId: Long): User?

    @Query("select * from user ORDER BY ID DESC LIMIT 1")
    fun getUser(): User?

    @Query("select * from user ORDER BY ID DESC LIMIT 1")
    fun getLoadUser(): LiveData<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User): Long

    @Update
    fun updateUser(user: User)
}

@Dao
interface UserPersonalDataHistoryDao {
    @Query("select * from userpersonaldatahistory ORDER BY ID DESC LIMIT 1")
    fun getUserHistory(): UserPersonalDataHistory

    @Query("select * from userpersonaldatahistory")
    fun getLoadUserHistories(): LiveData<List<UserPersonalDataHistory>>

    @Query("select * from userpersonaldatahistory where id =:id")
    fun getSpecifiedHistory(id: Long): LiveData<UserPersonalDataHistory>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHistory(userPersonalDataHistory: UserPersonalDataHistory)

}

@Dao
interface DietHistoryDao {

    @Transaction
    @Query("select * from diethistory where date > :daysFrom and date < :daysTo")
    fun getDietHistoryWithFoodBetweenDays(daysFrom: Long, daysTo: Long): List<DietHistoryWithFoodAte>

    @Query("select * from diethistory ORDER BY ID DESC")
    fun getDietHistory(): LiveData<List<DietHistory>>

    @Query("select * from diethistory where id =:id")
    fun getSpecifiedDietHistory(id: Long): LiveData<DietHistory>

    @Query("select * from diethistory where date =:date")
    fun getByDateDietHistory(date: Date): DietHistory?

    @Query("select * from diethistory ORDER BY id DESC LIMIT 1")
    fun getLastDietHistory(): DietHistory?

    @Query("select * from diethistory ORDER BY id DESC LIMIT 1")
    fun getLastLoadDietHistory(): LiveData<DietHistory>

    @Query("delete from diethistory where date >:date")
    fun deleteAllAfterDay(date: Date)

    @Query("select * from diethistory where date =:date")
    fun getLoadByDateDietHistory(date: Date): LiveData<DietHistory>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHistory(dietHistory: DietHistory): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHistories(dietHistories: List<DietHistory>)

    @Update
    fun updateHistory(dietHistory: DietHistory)

    @Transaction
    @Query("SELECT * FROM diethistory where date =:date")
    fun getLoadDietHistoryWithFoodAte(date: Date): LiveData<DietHistoryWithFoodAte>

    @Transaction
    @Query("SELECT * FROM diethistory ORDER BY id DESC LIMIT 1")
    fun getLoadLastDietHistoryWithFoodAte(): LiveData<DietHistoryWithFoodAte>

    @Transaction
    @Query("SELECT * FROM diethistory where date =:date")
    fun getDietHistoryWithFoodAte(date: Date): DietHistoryWithFoodAte
}

@Dao
interface FoodDao {
    @Query("select * from food ORDER BY food_id DESC LIMIT 50")
    fun getFoods(): List<Food>

    @Query("select * from food where food_id =:id")
    fun getFood(id: Long): Food

    @Query("select * from food where food_description like :textDescription || '%' LIMIT 50")
    fun getFoodByDescription(textDescription: String): List<Food>

}

@Dao
interface FoodAteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFoodAte(foodAte: FoodAte)

    @Delete
    fun deleteFoodAte(foodAte: FoodAte)
}

@Dao
interface PersonalFoodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPersonalFood(food: PersonalFood)

    @Query("select * from personalfood where food_description like :textDescription || '%' LIMIT 50")
    fun getFoodByDescription(textDescription: String): List<PersonalFood>

    @Query("select * from personalfood LIMIT 50")
    fun getAllPersonalFood() : List<PersonalFood>

    @Query("select * from personalfood")
    fun getLoadAllPersonalFood() : LiveData<List<PersonalFood>>

    @Query("select * from personalfood where food_description like :textDescription || '%' LIMIT 50")
    fun getLoadFoodByDescription(textDescription: String): LiveData<List<PersonalFood>>

    @Delete
    fun deletePersonalFood(personalFood: PersonalFood)
}

@Dao
interface FactorDao {
    @Query("select * from factor where id = 1")
    fun getLoadFactor() : LiveData<Factor>

    @Update
    fun updateFactor(factor: Factor)
}