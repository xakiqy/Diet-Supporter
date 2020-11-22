package com.xakiqy.diet_supporter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import timber.log.Timber
import timber.log.Timber.DebugTree


class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.plant(DebugTree())
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
    }
}