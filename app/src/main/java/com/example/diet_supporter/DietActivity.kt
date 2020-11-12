package com.example.diet_supporter

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.diet_supporter.databinding.ActivityDietBinding
import timber.log.Timber


class DietActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var binding: ActivityDietBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.plant(Timber.DebugTree())
        super.onCreate(savedInstanceState)
        @Suppress("UNUSED_VARIABLE")
        binding = DataBindingUtil.setContentView(this, R.layout.activity_diet)
        drawerLayout = binding.drawerLayout
        val navController = this.findNavController(R.id.myNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        // prevent nav gesture if not on start destination
        navController.addOnDestinationChangedListener { nc: NavController, nd: NavDestination, bundle: Bundle? ->
            if (nd.id == nc.graph.startDestination || nd.id == R.id.foodTabFragment || nd.id == R.id.personalFoodTabFragment) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            } else {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }
        }
        NavigationUI.setupWithNavController(binding.navView, navController)

        binding.bottomNav.setOnNavigationItemSelectedListener { item ->
            selectFragment(item)
            false
        }
        actionBar?.setDisplayHomeAsUpEnabled(false);
        navController.addOnDestinationChangedListener { _, nd, _ ->
            when (nd.id) {
                R.id.frontTabFragment -> {
                    binding.bottomNav.visibility =
                        View.VISIBLE; binding.bottomNav.menu.getItem(0).isChecked = true
                }
                R.id.foodTabFragment -> {
                    binding.bottomNav.visibility =
                        View.VISIBLE; binding.bottomNav.menu.getItem(1).isChecked = true
                }
                R.id.personalFoodTabFragment -> {
                    binding.bottomNav.visibility =
                        View.VISIBLE; binding.bottomNav.menu.getItem(2).isChecked = true
                }
                else -> binding.bottomNav.visibility = View.GONE
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }

    private fun selectFragment(item: MenuItem) {
        val navController = this.findNavController(R.id.myNavHostFragment)
        if (item.itemId == navController.currentDestination!!.id) {
            return
        }
        when (item.itemId) {
            R.id.frontTabFragment ->
                if (navController.currentDestination!!.id == R.id.foodTabFragment)
                    navController.navigateUp()
                else {
                    navController.navigateUp()
                    navController.navigateUp()
                }
            R.id.foodTabFragment ->
                if (navController.currentDestination!!.id == R.id.frontTabFragment)
                    navController.navigate(R.id.action_frontTabFragment_to_foodTabFragment)
                else
                    navController.navigateUp()
            R.id.personalFoodTabFragment ->
                if (navController.currentDestination!!.id == R.id.frontTabFragment) {
                    navController.navigate(R.id.action_frontTabFragment_to_foodTabFragment)
                    navController.navigate(R.id.action_foodTabFragment_to_personalFoodTabFragment)
                } else
                    navController.navigate(R.id.action_foodTabFragment_to_personalFoodTabFragment)
            else -> item.itemId
        }
    }
}