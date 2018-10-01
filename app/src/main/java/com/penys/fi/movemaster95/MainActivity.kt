package com.penys.fi.movemaster95

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    val manager = fragmentManager
    val BACK_STACK = "root_fragment"

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                manager.beginTransaction()
                        .replace(R.id.fragment_container, HomeFragment())
                        .addToBackStack(BACK_STACK)
                        .commit()

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_map -> {
                manager.beginTransaction()
                        .replace(R.id.fragment_container, MapFragment())
                        .addToBackStack(null)
                        .commit()

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {
                manager.beginTransaction()
                        .replace(R.id.fragment_container, ProfileFragment())
                        .addToBackStack(null)
                        .commit()

                return@OnNavigationItemSelectedListener true

            }
            R.id.navigation_settings -> {
                manager.beginTransaction()
                        .replace(R.id.fragment_container, SettingsFragment())
                        .addToBackStack(null)
                        .commit()

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

        manager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment())
                .addToBackStack(BACK_STACK)
                .commit()
    }
}
