package com.example.diet_supporter.util

import android.content.Context

fun longToGender(value: Long): Gender {
    when (value) {
        0.toLong() -> return Gender.MAN
    }
    return Gender.WOMAN
}

fun genderToLong(gender: Gender?): Long {
    return when (gender) {
        Gender.MAN -> 0
        else -> 1
    }
}

fun dipToFloat(context: Context, textSize: Int): Float {
    val scale = context.resources.displayMetrics.scaledDensity;
    return context.resources.getDimensionPixelSize(textSize) / scale
}

fun activityOfSpinnerPosition(pos: Int): PhysicalActivity {
    when (pos) {
        0 -> return PhysicalActivity.CHILL
        1 -> return PhysicalActivity.ORDINARY
        2 -> return PhysicalActivity.LIGHTTRAINING
        3 -> return PhysicalActivity.TRAINING
        4 -> return PhysicalActivity.HEAVYLIFTING
    }
    return PhysicalActivity.ORDINARY
}

fun activityToSpinnerPosition(pa: PhysicalActivity): Int {
    return when (pa) {
        PhysicalActivity.CHILL -> 0
        PhysicalActivity.ORDINARY -> 1
        PhysicalActivity.LIGHTTRAINING -> 2
        PhysicalActivity.TRAINING -> 3
        PhysicalActivity.HEAVYLIFTING -> 4
    }
}

fun difficultyOfSpinnerPosition(pos: Int): DietDifficulty {
    when (pos) {
        0 -> return DietDifficulty.EASY
        1 -> return DietDifficulty.MEDIUM
        2 -> return DietDifficulty.HARD
    }
    return DietDifficulty.EASY
}

fun difficultyToSpinnerPosition(dd: DietDifficulty): Int {
    return when (dd) {
        DietDifficulty.EASY -> 0
        DietDifficulty.MEDIUM -> 1
        DietDifficulty.HARD -> 2
    }
}