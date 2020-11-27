package com.xakiqy.diet_supporter.di

import android.content.Context
import androidx.room.Room
import com.xakiqy.diet_supporter.R
import com.xakiqy.diet_supporter.database.UserDietDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton


/**
 * Module to tell Hilt how to provide instances of types that cannot be constructor-injected.
 *
 * As these types are scoped to the application lifecycle using @Singleton, they're installed
 * in Hilt's ApplicationComponent.
 */
@Module
@InstallIn(ApplicationComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): UserDietDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            UserDietDatabase::class.java,
            "user_diet"
        )
            .createFromAsset(context.getString(R.string.database))
//                .fallbackToDestructiveMigration()
            .build()
    }
}
