package com.example.diet_supporter.util

import com.example.diet_supporter.database.LocalUserDiet

fun getCaloriesBasedRequired(user: LocalUserDiet): Double {
    if (user.gender.gender) {
        return 10 * user.weight + 6.25 * user.height - 5 * user.age - 161
    }
    return 10 * user.weight + 6.25 * user.height - 5 * user.age - 5
}

fun getCaloriesNeed(user: LocalUserDiet): Double {
    val caloriesBased = getCaloriesBasedRequired(user)

    return caloriesBased.times(user.physicalActivity!!.coef).times(user.dietDifficulty!!.calories)
}

fun getProteinNeed(calories: Double): Double {
    return calories.times(0.30) / 4
}

fun getCarbohydrateNeed(calories: Double): Double {
    return calories.times(0.40) / 4
}

fun getFatNeed(calories: Double): Double {
    return calories.times(0.30) / 9
}