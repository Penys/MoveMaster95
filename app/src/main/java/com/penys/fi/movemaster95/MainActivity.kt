package com.penys.fi.movemaster95

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    val manager = fragmentManager

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                val homeFragment = HomeFragment()
                val transaction = manager.beginTransaction()

                transaction.replace(R.id.fragment_container, homeFragment)
                transaction.addToBackStack(null)
                transaction.commit()

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_map -> {
                val mapFragment = MapFragment()
                val transaction = manager.beginTransaction()

                transaction.replace(R.id.fragment_container, mapFragment)
                transaction.addToBackStack(null)
                transaction.commit()

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {
                val statsFragment = StatsFragment()
                val transaction = manager.beginTransaction()

                transaction.replace(R.id.fragment_container, statsFragment)
                transaction.addToBackStack(null)
                transaction.commit()

                return@OnNavigationItemSelectedListener true

            }
            R.id.navigation_settings -> {
                val settingsFragment = SettingsFragment()
                val transaction = manager.beginTransaction()

                transaction.replace(R.id.fragment_container, settingsFragment)
                transaction.addToBackStack(null)
                transaction.commit()

                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this as Activity,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 0)

            // Permission is not granted
        } else {
        }

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val homeFragment = HomeFragment()
        val transaction = manager.beginTransaction()

        transaction.replace(R.id.fragment_container, homeFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
