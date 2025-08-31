package com.example.brewbuddy.feature.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.brewbuddy.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host) as NavHostFragment
        navController = navHostFragment.navController

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menuFragment -> {
                    navController.popBackStack(R.id.menuFragment, false)
                    navController.navigate(R.id.menuFragment)
                    true
                }
                R.id.favoritesFragment -> {
                    navController.popBackStack(R.id.favoritesFragment, false)
                    navController.navigate(R.id.favoritesFragment)
                    true
                }
                R.id.homeFragment -> {
                    navController.popBackStack(R.id.homeFragment, false)
                    navController.navigate(R.id.homeFragment)
                    true
                }
                R.id.ordersFragment -> {
                    navController.popBackStack(R.id.ordersFragment, false)
                    navController.navigate(R.id.ordersFragment)
                    true
                }
                else -> false
            }
        }

        bottomNav.setOnItemReselectedListener { item ->
            when (item.itemId) {
                R.id.menuFragment -> navController.popBackStack(R.id.menuFragment, false)
                R.id.favoritesFragment -> navController.popBackStack(R.id.favoritesFragment, false)
                R.id.homeFragment -> navController.popBackStack(R.id.homeFragment, false)
                R.id.ordersFragment -> navController.popBackStack(R.id.ordersFragment, false)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
