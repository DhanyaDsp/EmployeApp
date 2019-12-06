package com.ey.pwbc.ui.dashboard

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.View
import com.ey.pwbc.R
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.ey.pwbc.model.User
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.app_bar_landing.*
import androidx.appcompat.app.AppCompatDelegate

class LandingActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {
    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        if (destination.id == R.id.nav_merchant_welcome) {
            showProgress(false)
        } else {
            showProgress(true)
        }
    }

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout
    var toolbar: Toolbar? = null;
    var user = User()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        initToolbar();
        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = this.findViewById(R.id.nav_view)

        navView.inflateMenu(R.menu.menu_merchant)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_wallet,
                R.id.nav_profile,
                R.id.nav_emessi,
                R.id.nav_transferiti,
                R.id.nav_logout
            ), drawerLayout
        )

        val navController = findNavController(R.id.nav_host_fragment)
        navController.addOnDestinationChangedListener(this)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        moveToMerchantWelcomeScreen()

    }

    @SuppressLint("RestrictedApi")
    private fun initToolbar() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(false)
        tvTitle.text = getString(R.string.app_name)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.landing, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun showProgress(show: Boolean) {
        if (show) {
            toolbar?.visibility = View.VISIBLE
        } else {
            toolbar?.visibility = View.GONE
        }
    }

    private fun moveToMerchantWelcomeScreen() {
        val navController = this.findNavController(R.id.nav_host_fragment)
        navController.navigate(R.id.nav_merchant_welcome)
    }

}
