@file:Suppress("DEPRECATION")

package com.penys.fi.movemaster95

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val manager = this.fragmentManager

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                manager.beginTransaction()
                        .replace(R.id.fragment_container, HomeFragment())
                        .addToBackStack(null)
                        .commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_map -> {

                mapsActivity()
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
            ActivityCompat.requestPermissions(this as Activity,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 0)

        } else {
            Log.d("dbg", "Permission Ok")
        }

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        manager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment())
                .addToBackStack(null)
                .commit()
    }

    fun mapsActivity() {
        val intent = Intent(this, MapsActivity::class.java).apply {
        }
        startActivity(intent)
    }
}
