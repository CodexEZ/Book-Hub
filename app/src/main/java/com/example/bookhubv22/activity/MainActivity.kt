package com.example.bookhubv22.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar;
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.bookhubv22.fragment.DashboardFragment
import com.example.bookhubv22.R
import com.example.bookhubv22.fragment.aboutFragment
import com.example.bookhubv22.fragment.favouritesFragment
import com.example.bookhubv22.fragment.profileFragment
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    var dash = "Dashboard"
    var fav = "Favourites"
    var prof = "Profile"
    var about = "About"
    var previousMenuItem: MenuItem? = null
    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var toolbar: Toolbar
    lateinit var frameLayout: FrameLayout
    lateinit var navigationView: NavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        coordinatorLayout = findViewById(R.id.coordinatorlayout)
        toolbar = findViewById(R.id.toolbar)
        navigationView = findViewById(R.id.navigationview)
        drawerLayout = findViewById(R.id.drawerlayout)
        frameLayout = findViewById(R.id.frame)
        setUpToolBar()

        val actionBarDrawerToggle = ActionBarDrawerToggle(this@MainActivity,drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        supportFragmentManager.beginTransaction().replace(R.id.frame, DashboardFragment()).commit()
        supportActionBar?.title = dash
        navigationView.setCheckedItem(R.id.dashboard)

            navigationView.setNavigationItemSelectedListener {
                if(previousMenuItem != null){
                    previousMenuItem?.isChecked = false
                }
                it.isCheckable = true
                it.isChecked = true
                previousMenuItem = it
                when(it.itemId){
                    R.id.dashboard ->{
                        supportFragmentManager.beginTransaction().addToBackStack("Dashboard").replace(
                            R.id.frame,
                            DashboardFragment()
                        ).commit()
                        supportActionBar?.title = dash
                        drawerLayout.closeDrawers()
                    }

                    R.id.favourites ->{
                        supportActionBar?.title = fav
                        supportFragmentManager.beginTransaction().addToBackStack("Favourites").replace(
                            R.id.frame,
                            favouritesFragment()
                        ).commit()
                        drawerLayout.closeDrawers()
                    }

                    R.id.profile -> {
                        supportActionBar?.title = prof
                        supportFragmentManager.beginTransaction().addToBackStack("Profile").replace(
                            R.id.frame,
                            profileFragment()
                        ).commit()
                        drawerLayout.closeDrawers()
                    }

                    R.id.aboutApp -> {
                        supportActionBar?.title = about
                        supportFragmentManager.beginTransaction().addToBackStack("About").replace(
                            R.id.frame,
                            aboutFragment()
                        ).commit()
                        drawerLayout.closeDrawers()
                    }
                }
                return@setNavigationItemSelectedListener true
            }

    }
    fun setUpToolBar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Toolbar title"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if(id == android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }
}
