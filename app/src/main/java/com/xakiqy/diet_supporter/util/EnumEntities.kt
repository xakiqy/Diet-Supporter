package com.xakiqy.diet_supporter.util

enum class DietDifficulty(val calories: Double) {
    EASY(0.85),
    MEDIUM(0.80),
    HARD(0.75),
}

enum class PhysicalActivity(val coef: Double) {
    CHILL(1.2),
    ORDINARY(1.375),
    LIGHTTRAINING(1.55),
    TRAINING(1.725),
    HEAVYLIFTING(1.9)
}

enum class Gender(val gender: Boolean) {
    MAN(false),
    WOMAN(true)
}

enum class DayDirections {
    NEXT,
    PREVIOUS,
    HOLD,
}

enum class FoodEnergy {
    Calories,
    Carbs,
    Protein,
    Fat
}

