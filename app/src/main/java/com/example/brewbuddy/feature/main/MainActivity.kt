package com.example.brewbuddy.feature.main

import android.content.Intent
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.brewbuddy.R
import com.example.brewbuddy.core.prefs.PreferenceHelper
import com.example.brewbuddy.databinding.ActivityMainBinding
import com.example.brewbuddy.feature.onboarding.OnBoardingActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private var popup: PopupMenu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup NavController
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host) as NavHostFragment
        navController = navHostFragment.navController

        // Bottom Navigation
        binding.bottomNav.setupWithNavController(navController)
        binding.bottomNav.setOnItemReselectedListener { /* prevent reload */ }

        // Update header and bottom nav visibility based on destination
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val isDetailScreen = destination.id == R.id.drinkDetailsFragment
            binding.bottomNav.isVisible = !isDetailScreen
            binding.header.root.isVisible = !isDetailScreen


            val userName = getUserDisplayName()
            val title = when (destination.id) {
                R.id.homeFragment -> getString(R.string.header_good_day_with_name, userName)
                R.id.menuFragment -> getString(R.string.header_what_drink)
                R.id.ordersFragment -> getString(R.string.header_orders)
                R.id.favoritesFragment -> getString(R.string.header_favorites)
                else -> destination.label ?: getString(R.string.app_name)
            }
            binding.header.tvHeaderTitle.text = title
        }

        setupBottomNavClicks()
        setupHeaderPopup()
    }

    private fun setupBottomNavClicks() {
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment,
                R.id.menuFragment,
                R.id.favoritesFragment,
                R.id.ordersFragment -> {
                    navController.popBackStack(item.itemId, false)
                    navController.navigate(item.itemId)
                    true
                }
                else -> false
            }
        }

        binding.bottomNav.setOnItemReselectedListener { item ->
            navController.popBackStack(item.itemId, false)
        }
    }

    private fun setupHeaderPopup() {
        val iconGroup = binding.header.iconGroup
        iconGroup.setOnClickListener { view ->
            popup?.dismiss()

            val themed = ContextThemeWrapper(view.context, R.style.PopupMenuOverlay)
            val p = PopupMenu(themed, view)
            popup = p

            iconGroup.isActivated = true

            p.menuInflater.inflate(R.menu.menu_header_dropdown, p.menu)
            p.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_profile -> { Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show(); true }
                    R.id.action_settings -> { Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show(); true }
                    R.id.action_help -> { Toast.makeText(this, "Help", Toast.LENGTH_SHORT).show(); true }
                    R.id.action_logout -> {
                        PreferenceHelper.deleteUserName(this)
                        startActivity(
                            Intent(this, OnBoardingActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            }
                        )
                        Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> false
                }
            }

            p.setOnDismissListener { iconGroup.isActivated = false; popup = null }

            // Force show icons
            try {
                val f = p.javaClass.getDeclaredField("mPopup")
                f.isAccessible = true
                val helper = f.get(p)
                val m = helper.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.javaPrimitiveType)
                m.invoke(helper, true)
            } catch (_: Exception) {}

            p.show()
        }
    }

    private fun getUserDisplayName(): String = PreferenceHelper.getUserName(this).orEmpty()

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
