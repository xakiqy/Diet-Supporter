package com.xakiqy.diet_supporter.database

import android.os.Parcelable
import androidx.room.*
import com.xakiqy.diet_supporter.util.DietDifficulty
import com.xakiqy.diet_supporter.util.Gender
import com.xakiqy.diet_supporter.util.PhysicalActivity
import kotlinx.android.parcel.Parcelize
import java.util.*

@Entity
@Parcelize
data class User(
    @PrimaryKey(autoGenerate = true) val id: Long?,
    val name: String,
    val weight: Double,
    val height: Int,
    val age: Int,
    @TypeConverters(GenderConverter::class)
    val gender: Gender,
    @TypeConverters(DietDifficultyConverter::class)
    val dietDifficulty: DietDifficulty?,
    @TypeConverters(PhysicalActivityConverter::class)
    val physicalActivity: PhysicalActivity?
) : Parcelable {
    constructor(
        name: String,
        weight: Double,
        height: Int,
        age: Int,
        gender: Gender,
        dietDifficulty: DietDifficulty?,
        physicalActivity: PhysicalActivity?
    ) : this(null, name, weight, height, age, gender, dietDifficulty, physicalActivity)
}


@Entity
@Parcelize
data class UserPersonalDataHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Long?, val name: String, val weight: Double, val height: Int, val age: Int,
    @TypeConverters(GenderConverter::class)
    val gender: Gender,
    @TypeConverters(DietDifficultyConverter::class)
    val dietDifficulty: DietDifficulty,
    @TypeConverters(PhysicalActivityConverter::class)
    val physicalActivity: PhysicalActivity,
    val userId: Long?
) : Parcelable {
    constructor(
        name: String,
        weight: Double,
        height: Int,
        age: Int,
        gender: Gender,
        dietDifficulty: DietDifficulty,
        physicalActivity: PhysicalActivity,
        userId: Long?
    ) : this(
        null, name, weight, height, age, gender, dietDifficulty, physicalActivity, userId
    )
}

data class UserWithUserPersonalDataHistories(
    @Embedded val user: User,
    @Relation(
        parentColumn = "id",
        entityColumn = "userId"
    )
    val userPersonalDataHistories: List<UserPersonalDataHistory>
)

@Entity
@Parcelize
data class DietHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Long?,
    @TypeConverters(DateConverter::class)
    val date: Date,
    val userId: Long,
    val caloriesNeed: Double,
    val proteinNeed: Double,
    val carbohydrateNeed: Double,
    val fatNeed: Double,
) : Parcelable {
    constructor(
        date: Date,
        userId: Long,
        caloriesNeed: Double,
        proteinNeed: Double,
        carbohydrateNeed: Double,
        fatNeed: Double
    ) : this(
        null,
        date,
        userId,
        caloriesNeed,
        proteinNeed,
        carbohydrateNeed,
        fatNeed
    )
}

data class UserWithDietHistories(
    @Embedded val user: User,
    @Relation(
        parentColumn = "id",
        entityColumn = "userId"
    )
    val dietHistories: List<DietHistory>
)

data class DietHistoryWithFoodAte(
    @Embedded val dietHistory: DietHistory,
    @Relation(
        parentColumn = "id",
        entityColumn = "dietHistory_id"
    )
    val foodAte: List<FoodAte>
)

@Entity
@Parcelize
data class Food(
    @PrimaryKey(autoGenerate = true)
    val food_id: Long,
    val food_description: String,
    val energy: Double,
    val protein: Double,
    val carbohydrate: Double,
    val fat: Double,
) : Parcelable

@Entity
@Parcelize
data class FoodAte(
    @PrimaryKey(autoGenerate = true)
    val food_id: Long?,
    val food_description: String,
    val energy: Double,
    val protein: Double,
    val carbohydrate: Double,
    val fat: Double,
    val weight: Int,
    val dietHistory_id: Long?
) : Parcelable {
    constructor(
        food_description: String,
        energy: Double,
        protein: Double,
        carbohydrate: Double,
        fat: Double,
        weight: Int,
        dietHistory_id: Long?
    ) : this(null, food_description, energy, protein, carbohydrate, fat, weight, dietHistory_id)
}

@Entity
@Parcelize
data class PersonalFood(
    @PrimaryKey(autoGenerate = true)
    val food_id: Long?,
    val food_description: String,
    val energy: Double,
    val protein: Double,
    val carbohydrate: Double,
    val fat: Double,
    val weight: Int,
    val pathToImg: String
) : Parcelable {
    constructor(
        food_description: String,
        energy: Double,
        protein: Double,
        carbohydrate: Double,
        fat: Double,
        weight: Int,
        pathToImg: String
    ) : this(null, food_description, energy, protein, carbohydrate, fat, weight, pathToImg)
}

@Entity
data class Factor(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    var protein: Int,
    var carbohydrate: Int,
    var fat: Int,
    var custom: Int,
    var calories: Double
)

data class LocalUserDiet(
    val weight: Double,
    val height: Int,
    val age: Int,
    val gender: Gender,
    val dietDifficulty: DietDifficulty?,
    val physicalActivity: PhysicalActivity?
)

fun User.toLocalUser() = LocalUserDiet(
    this.weight,
    this.height,
    this.age,
    this.gender,
    this.dietDifficulty,
    this.physicalActivity
)

