package com.example.diet_supporter.util

import com.example.diet_supporter.database.Factor
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

fun getProteinNeed(calories: Double, factor: Factor): Double {
    return calories.times(factor.protein) / 4 / 100
}

fun getCarbohydrateNeed(calories: Double, factor: Factor): Double {
    return calories.times(factor.carbohydrate) / 4 / 100
}

fun getFatNeed(calories: Double, factor: Factor): Double {
    return calories.times(factor.fat) / 9 / 100
}