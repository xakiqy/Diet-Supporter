package com.example.diet_supporter.database

import androidx.room.TypeConverter
import com.example.diet_supporter.util.DietDifficulty
import com.example.diet_supporter.util.Gender
import com.example.diet_supporter.util.PhysicalActivity
import java.util.*

class DietDifficultyConverter {

    @TypeConverter
    fun toDietDifficulty(calories: Double): DietDifficulty {
        when (calories) {
            DietDifficulty.EASY.calories -> return DietDifficulty.EASY
            DietDifficulty.MEDIUM.calories -> return DietDifficulty.MEDIUM
            DietDifficulty.HARD.calories -> return DietDifficulty.HARD
        }
        return DietDifficulty.EASY
    }

    @TypeConverter
    fun fromDietDifficulty(df: DietDifficulty): Double {
        return df.calories
    }
}

class PhysicalActivityConverter{

    @TypeConverter
    fun toPhysicalActivity(coef: Double) : PhysicalActivity {
        when(coef) {
            PhysicalActivity.CHILL.coef -> return PhysicalActivity.CHILL
            PhysicalActivity.ORDINARY.coef -> return PhysicalActivity.ORDINARY
            PhysicalActivity.LIGHTTRAINING.coef -> return PhysicalActivity.LIGHTTRAINING
            PhysicalActivity.TRAINING.coef -> return PhysicalActivity.TRAINING
            PhysicalActivity.HEAVYLIFTING.coef -> return PhysicalActivity.HEAVYLIFTING
        }
        return PhysicalActivity.CHILL
    }

    @TypeConverter
    fun fromPhysicalActivity(pa: PhysicalActivity) : Double {
        return pa.coef
    }

}

class GenderConverter {
    @TypeConverter
    fun toGender(genderInt: Int?): Gender? {
        when(genderInt){
            0 -> return Gender.MAN
        }
        return Gender.WOMAN
    }

    @TypeConverter
    fun fromGender(gender: Gender?): Int? {
        return when(gender){
            Gender.MAN -> 0
            else -> 1
        }
    }
}

class DateConverter {
    @TypeConverter
    fun toDate(dateLong: Long?): Date? {
        return dateLong?.let { Date(it) }
    }

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }
}